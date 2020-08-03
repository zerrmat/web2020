package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.marketstack.service.ExchangeMarketStackService;
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
    private ExchangeMarketStackService exchangeMarketStackService;
    private ExchangeService exchangeService;
    private CacheControlService cacheControlService;

    @Autowired
    public MarketStackController(ExchangeMarketStackService service,
                                 ExchangeService exchangeService,
                                 CacheControlService cacheControlService) {
        this.exchangeMarketStackService = service;
        this.exchangeService = exchangeService;
        this.cacheControlService = cacheControlService;
    }

    private String makeMarketStackRequest() {
        WebClient webClient = WebClient.create();

        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(URI.create("http://api.marketstack.com/v1/exchanges?access_key=166af8c956780fd148bc9dd925968daf"));

        return Objects.requireNonNull(requestHeadersSpec.exchange()
                .block())
                .bodyToMono(String.class)
                .block();
    }

    private void updateCacheControl() {
        LocalDateTime requestTimestamp = LocalDateTime.now();

        cacheControlService.updateOne(
                CacheControlDto.builder()
                .endpointName("exchanges")
                .lastAccess(requestTimestamp)
                .build()
        );
    }

    @GetMapping("/exchanges")
    public void updateExchanges() {
        CacheControlDto cacheControlDto = cacheControlService.getOne("exchanges");

        if (cacheControlDto != null) {
            if (cacheControlDto.getLastAccess().isBefore(LocalDateTime.now().minusDays(1))) {
                String response = this.makeMarketStackRequest();

                try {
                    ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper()
                            .readValue(response, new TypeReference<ExchangeMarketStackResponseWrapper>(){});
                    List<ExchangeDto> exchangeDtos = responseWrapper.extract();

                    List<ExchangeDto> exchangesInDB = exchangeService.getAll();
                    exchangeMarketStackService.updateExchanges(exchangeDtos, exchangesInDB);

                    this.updateCacheControl();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
