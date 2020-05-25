package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.finnhub.service.ExchangeFinnhubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

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
            .uri(URI.create("https://finnhub.io/api/v1/stock/exchange"))
            .attribute("token", "br5qjnvrh5rdamtp47j0");

        String response = requestHeadersSpec.exchange()
            .block()
            .bodyToMono(String.class)
            .block();

        System.out.println(response);
    }
}
