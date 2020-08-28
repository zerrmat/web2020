package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Objects;

@Service
public class ExternalRequests {
    public String makeMarketStackStocksRequest(MarketStackPagination pagination, String exchangeId) {
        final String urlEndpointAddress = "http://api.marketstack.com/v1/exchanges/" + exchangeId + "/tickers";
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
}
