package com.zerrmat.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.marketstack.service.ExchangeMarketStackService;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManagerFactory;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MarketStackController {
    private ExchangeMarketStackService exchangeMarketStackService;
    private ExchangeService exchangeService;

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public MarketStackController(EntityManagerFactory entityManagerFactory,
                                 ExchangeMarketStackService service,
                                 ExchangeService exchangeService) {
        this.entityManagerFactory = entityManagerFactory;
        this.exchangeMarketStackService = service;
        this.exchangeService = exchangeService;
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

        List<ExchangeDto> exchangesInDB = exchangeService.getAll();

        try {
            //List<ExchangeMarketStackResponse> responses = new ObjectMapper().readValue(response,
              //      new TypeReference<>(){});
            ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(response,
                    new TypeReference<>(){});
            List<ExchangeMarketStackResponse> responses = responseWrapper.extractResponse();

            Set<String> responseCodes = responses.stream()
                    .map(ExchangeMarketStackResponse::getMic)
                    .collect(Collectors.toSet());

            List<ExchangeDto> obsoleteExchanges = exchangesInDB.stream()
                    .filter(e -> !responseCodes.contains(e.getCode()))
                    .collect(Collectors.toList());


            String hql = "delete from ExchangeModel where code = :code";
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.createQuery(hql)
                    .setParameter("code", obsoleteExchanges.get(0).getCode())
                    .executeUpdate();
            transaction.commit();
            session.close();

            ExchangeMarketStackRequest request = new ExchangeMarketStackRequest();
            request.setElements(responses);
            exchangeMarketStackService.save(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
