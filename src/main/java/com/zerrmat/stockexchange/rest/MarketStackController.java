package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.marketstack.service.ExchangeMarketStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;

@Controller
public class MarketStackController {
    private ExchangeMarketStackService exchangeMarketStackService;

    @Autowired
    public MarketStackController(ExchangeMarketStackService service) {
        this.exchangeMarketStackService = service;
    }

    @GetMapping("/test")
    public void getExchanges() {
        WebClient webClient = WebClient.create();

        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(URI.create("http://api.marketstack.com/v1/exchanges?access_key=166af8c956780fd148bc9dd925968daf"));

        String response = requestHeadersSpec.exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        try {
            //List<ExchangeMarketStackResponse> responses = new ObjectMapper().readValue(response,
              //      new TypeReference<>(){});
            ExchangeMarketStackResponse responses = new ObjectMapper().readValue(response,
                    new TypeReference<>(){});
            ExchangeMarketStackRequest request = new ExchangeMarketStackRequest();
            //request.setElements(responses);
            exchangeMarketStackService.save(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
