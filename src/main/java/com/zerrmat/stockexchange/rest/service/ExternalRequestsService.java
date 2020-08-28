package com.zerrmat.stockexchange.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExternalRequestsService {
    private final int oneRequestDtoLimit = 1000;

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
