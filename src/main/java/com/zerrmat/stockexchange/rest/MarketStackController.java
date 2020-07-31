package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.marketstack.service.ExchangeMarketStackService;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManagerFactory;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
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

    @GetMapping("/test")
    public List<ExchangeMarketStackResponse> getExchanges() {
        CacheControlDto cacheControlDto = cacheControlService.getOne("exchanges");

        List<ExchangeMarketStackResponse> responses = new ArrayList<>();

        if (cacheControlDto != null) {
            if (cacheControlDto.getLastAccess().isBefore(LocalDateTime.now().minusDays(1))) {
                WebClient webClient = WebClient.create();

                WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                        .get()
                        .uri(URI.create("http://api.marketstack.com/v1/exchanges?access_key=166af8c956780fd148bc9dd925968daf"));

                String response = requestHeadersSpec.exchange()
                        .block()
                        .bodyToMono(String.class)
                        .block();

                LocalDateTime requestTimestamp = LocalDateTime.now();

                try {
                    ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(response,
                            new TypeReference<>(){});
                    responses = responseWrapper.extractResponse();

                    List<ExchangeDto> exchangesInDB = exchangeService.getAll();
                    exchangeMarketStackService.updateExchanges(responses, exchangesInDB);

                    CacheControlDto cacheControlExchangesDto = CacheControlDto.builder()
                            .endpointName("exchanges")
                            .lastAccess(requestTimestamp)
                            .build();
                    cacheControlService.updateOne(cacheControlExchangesDto);

                    ExchangeMarketStackRequest request = new ExchangeMarketStackRequest();
                    request.setElements(responses);
                    exchangeMarketStackService.save(request);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                // get exchanges from cache
                responses = exchangeMarketStackService.getAll();
            }
        }
        return responses;
    }
}
