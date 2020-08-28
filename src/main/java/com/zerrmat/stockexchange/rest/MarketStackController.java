package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* TODO: real data for stocks from specific exchange, data has to be taken from:
    http://api.marketstack.com/v1/tickers/AAPL/intraday?access_key=166af8c956780fd148bc9dd925968daf&limit=1&symbols=AAPL&interval=15min (stock actual day, real-time data)
*/

@RestController
@RequestMapping("/external")
public class MarketStackController {
    private ExchangeService exchangeService;
    private StockService stockService;
    private CacheControlService cacheControlService;
    private ExternalRequests externalRequests;
    private int oneRequestDtoLimit = 1000;

    @Autowired
    public MarketStackController(ExchangeService exchangeService,
                                 StockService stockService,
                                 CacheControlService cacheControlService,
                                 ExternalRequests externalRequests) {
        this.exchangeService = exchangeService;
        this.stockService = stockService;
        this.cacheControlService = cacheControlService;
        this.externalRequests = externalRequests;
    }

    @GetMapping("/exchanges")
    public void updateExchanges() {
        String exchangesEndpointName = "exchanges";
        CacheControlDto cacheControlDto = cacheControlService.getCacheDataFor(exchangesEndpointName);
        if (cacheControlDto != null && !cacheControlDto.isCacheOutdated()) {
            return;
        }

        String response = this.makeMarketStackExchangesRequest();
        try {
            ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper()
                    .readValue(response, new TypeReference<ExchangeMarketStackResponseWrapper>(){});
            List<ExchangeDto> actualExchangeDtos = responseWrapper.extract();
            exchangeService.updateExchanges(actualExchangeDtos);

            cacheControlService.updateOne(
                    CacheControlDto.builder()
                            .endpointName(exchangesEndpointName)
                            .lastAccess(LocalDateTime.now())
                            .build()
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exchanges/{exchangeId}/stocks")
    public List<StockDto> updateStocks(@PathVariable String exchangeId) {
        String stocksEndpointName = "stocks." + exchangeId;
        if (!hasShouldUpdate(stocksEndpointName)) {
            return new ArrayList<>();
        }

        List<StockDto> actualStockDtos = this.accumulateData(exchangeId);
        stockService.updateStocks(actualStockDtos, exchangeService.get(exchangeId));

        cacheControlService.updateOne(
                CacheControlDto.builder()
                        .endpointName(stocksEndpointName)
                        .lastAccess(LocalDateTime.now())
                        .build()
        );

        return actualStockDtos;
    }

    private List<StockDto> accumulateData(String exchangeId) {
        List<StockDto> obtainedStockDtos = new ArrayList<>();
        MarketStackPagination pagination = new MarketStackPagination();
        do {
            String response = externalRequests.makeMarketStackStocksRequest(pagination, exchangeId);

            try {
                StockMarketStackResponseWrapper responseWrapper = new ObjectMapper()
                        .readValue(response, new TypeReference<StockMarketStackResponseWrapper>(){});
                pagination = responseWrapper.getPagination();
                pagination.setOffset(pagination.getOffset() + oneRequestDtoLimit);
                obtainedStockDtos.addAll(responseWrapper.extract(exchangeService.get(exchangeId).getCurrency()));
            } catch(JsonProcessingException e) {
                e.printStackTrace();
            }
        } while (pagination.getOffset() < pagination.getTotal());
        return obtainedStockDtos;
    }

    private boolean hasShouldUpdate(String stocksEndpointName) {
        CacheControlDto cacheControlDto = cacheControlService.getCacheDataFor(stocksEndpointName);
        if (cacheControlDto != null && !cacheControlDto.isCacheOutdated()) {
            return false;
        }
        return true;
    }

    private String makeMarketStackExchangesRequest() {
        final String urlEndpointAddress = "http://api.marketstack.com/v1/exchanges";
        final String accessKey = "166af8c956780fd148bc9dd925968daf";

        StringBuilder sb = new StringBuilder();
        String fullURL = sb.append(urlEndpointAddress).append("?").append("access_key=")
                .append(accessKey).toString();

        WebClient webClient = WebClient.create();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(URI.create(fullURL));

        return Objects.requireNonNull(requestHeadersSpec.exchange()
                .block())
                .bodyToMono(String.class)
                .block();
    }

}
