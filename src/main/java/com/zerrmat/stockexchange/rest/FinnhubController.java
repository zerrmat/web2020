package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.exchange.finnhub.dto.ExchangeFinnhubResponse;
import com.zerrmat.stockexchange.exchange.finnhub.service.ExchangeFinnhubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Controller
public class FinnhubController {
    private ExchangeFinnhubService exchangeFinnhubService;

    @Autowired
    public FinnhubController(ExchangeFinnhubService exchangeFinnhubService) {
        this.exchangeFinnhubService = exchangeFinnhubService;
    }

    @GetMapping("/test")
    public void getExchanges() {
       WebClient webClient = WebClient.create();

       WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
            .get()
            .uri(URI.create("https://finnhub.io/api/v1/stock/exchange?token=bs82227rh5r8i6g9aeag"));

       String response = requestHeadersSpec.exchange()
            .block()
            .bodyToMono(String.class)
            .block();

       try {
           List<ExchangeFinnhubResponse> responses = new ObjectMapper().readValue(response,
                   new TypeReference<>(){});
           ExchangeFinnhubRequest request = new ExchangeFinnhubRequest();request.setElements(responses);
           exchangeFinnhubService.save(request);
       } catch (JsonProcessingException e) {
           System.err.println("Oops!");
       }
    }
}
