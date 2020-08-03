package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/external")
public class MarketStackController {
    private ExchangeService exchangeService;
    private CacheControlService cacheControlService;

    @Autowired
    public MarketStackController(ExchangeService exchangeService,
                                 CacheControlService cacheControlService) {
        this.exchangeService = exchangeService;
        this.cacheControlService = cacheControlService;
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
