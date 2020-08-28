package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.rest.ExternalExchangesController;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stockexchange.util.RestTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ExternalExchangesControllerTest {
    private ExternalExchangesController externalExchangesController;

    @Mock
    private CacheControlService cacheControlService;
    @Mock
    private ExternalRequestsService externalRequestsService;
    @Mock
    private ExchangeService exchangeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        externalExchangesController = new ExternalExchangesController(cacheControlService,
                externalRequestsService, exchangeService);
    }

    @Test
    public void shouldConsumeResponse() throws JsonProcessingException {
        // given
        String responseFilename = "MarketStackExchangeRequest.json";
        String responseBody = RestTestUtils.generateResponseBody(responseFilename);

        ExchangeMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(
                responseBody, new TypeReference<ExchangeMarketStackResponseWrapper>(){});
        List<ExchangeDto> responseDtos = responseWrapper.extract();

        Mockito.when(externalRequestsService.makeExternalMarketStackExchangesRequest())
                .thenReturn(responseBody);
        Mockito.when(externalRequestsService.makeMarketStackExchangesRequest(exchangeService))
                .thenReturn(responseDtos);

        // when
        List<ExchangeDto> result = externalExchangesController.executeEndpoint();
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for (int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getName()).isEqualTo(responseDtos.get(i).getName());
            Assertions.assertThat(result.get(i).getSymbol()).isEqualTo(responseDtos.get(i).getSymbol());
            Assertions.assertThat(result.get(i).getCurrency()).isEqualTo(responseDtos.get(i).getCurrency());
        }
    }
}
