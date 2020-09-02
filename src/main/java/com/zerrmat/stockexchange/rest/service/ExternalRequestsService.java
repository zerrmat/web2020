package com.zerrmat.stockexchange.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.ticker.marketstack.dto.TickerEODLatestMarketStackResponseWrapper;
import com.zerrmat.stockexchange.util.ZonedDateTimeDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExternalRequestsService {
    private final int oneRequestDtoLimit = 1000;

    public List<TickerDto> makeMarketStackTickersRequest(String stockSymbol) {
        String response = this.makeExternalMarketStackTickersRequest(stockSymbol);
        List<TickerDto> obtainedTickerDtos = new ArrayList<>();
        try {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            TickerEODLatestMarketStackResponseWrapper responseWrapper = objectMapper.readValue(
                    response, new TypeReference<TickerEODLatestMarketStackResponseWrapper>(){});
            obtainedTickerDtos.add(responseWrapper.extract());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return obtainedTickerDtos;
    }

    public String makeExternalMarketStackTickersRequest(String stockSymbol) {
        final String urlEndpointAddress = "http://api.marketstack.com/v1/tickers/" + stockSymbol + "/eod/latest";
        final String accessKey = "166af8c956780fd148bc9dd925968daf";

        String fullUrl = urlEndpointAddress + "?" + "access_key=" + accessKey;

        WebClient webClient = WebClient.create();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(URI.create(fullUrl));

        return Objects.requireNonNull(requestHeadersSpec.exchange())
                .block()
                .bodyToMono(String.class)
                .block();
    }

    public List<StockDto> makeMarketStackStocksRequest(String exchangeSymbol, ExchangeService exchangeService) {
        List<StockDto> obtainedStockDtos = new ArrayList<>();
        MarketStackPagination pagination = new MarketStackPagination();
        do {
            String response = this.makeExternalMarketStackStocksRequest(pagination, exchangeSymbol);

            try {
                StockMarketStackResponseWrapper responseWrapper = new ObjectMapper()
                        .readValue(response, new TypeReference<StockMarketStackResponseWrapper>(){});
                pagination = responseWrapper.getPagination();
                pagination.setOffset(pagination.getOffset() + oneRequestDtoLimit);
                obtainedStockDtos.addAll(responseWrapper.extract(exchangeService.getBySymbol(exchangeSymbol).getCurrency()));
            } catch(JsonProcessingException e) {
                e.printStackTrace();
            }
        } while (pagination.getOffset() < pagination.getTotal());
        return obtainedStockDtos;
    }

    public String makeExternalMarketStackStocksRequest(MarketStackPagination pagination, String exchangeSymbol) {
        final String urlEndpointAddress = "http://api.marketstack.com/v1/exchanges/" + exchangeSymbol + "/tickers";
        final String accessKey = "166af8c956780fd148bc9dd925968daf";

        String fullUrl = urlEndpointAddress + "?" + "access_key=" + accessKey + "&limit=1000" + "&offset=" + (pagination.getOffset());

        WebClient webClient = WebClient.create();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(URI.create(fullUrl));

        return Objects.requireNonNull(requestHeadersSpec.exchange())
                .block()
                .bodyToMono(String.class)
                .block();
    }

    public List<ExchangeDto> makeMarketStackExchangesRequest(ExchangeService exchangeService) {
        List<ExchangeDto> actualExchangeDtos = new ArrayList<>();

        String response = this.makeExternalMarketStackExchangesRequest();
        try {
            ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper()
                    .readValue(response, new TypeReference<ExchangeMarketStackResponseWrapper>(){});
            actualExchangeDtos = responseWrapper.extract();
            exchangeService.updateExchanges(actualExchangeDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return actualExchangeDtos;
    }

    public String makeExternalMarketStackExchangesRequest() {
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
