package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.rest.ExternalRequests;
import com.zerrmat.stockexchange.rest.MarketStackController;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.stock.service.StockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class MarketStackControllerTest {
    private MarketStackController controller;

    @Mock
    private ExchangeService exchangeService;
    @Mock
    private StockService stockService;
    @Mock
    private CacheControlService cacheControlService;
    @Mock
    private ExternalRequests externalRequests;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new MarketStackController(exchangeService, stockService, cacheControlService, externalRequests);
    }

    @Test
    public void shouldConsumeResponse() throws JsonProcessingException {
        // given
        String exchangeId = "XWAR";

        StockMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(
                this.generateRequestBody("MarketStackStockRequestWarsaw.json"),
                new TypeReference<StockMarketStackResponseWrapper>(){});
        List<StockDto> responseDtos = responseWrapper.extract("EUR");
        CacheControlDto cacheControlDto = CacheControlDto.builder()
                .endpointName("stocks.XWAR")
                .lastAccess(LocalDateTime.now().minusDays(2))
                .build();
        Mockito.when(cacheControlService.getCacheDataFor("stocks.XWAR")).thenReturn(cacheControlDto);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol("XWAR")
                .name("Warsaw Stock Exchange")
                .currency("EUR")
                .build();
        Mockito.when(exchangeService.get(exchangeId)).thenReturn(exchangeDto);

        Mockito.when(externalRequests.makeMarketStackStocksRequest(any(MarketStackPagination.class), any(String.class)))
                .thenReturn(this.generateRequestBody("MarketStackStockRequestWarsaw.json"));

        // when
        List<StockDto> result = controller.updateStocks(exchangeId);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for(int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getName()).isEqualTo(responseDtos.get(i).getName());
            Assertions.assertThat(result.get(i).getSymbol()).isEqualTo(responseDtos.get(i).getSymbol());
            Assertions.assertThat(result.get(i).getValue()).isEqualTo(responseDtos.get(i).getValue());
        }
    }

    @Test
    public void shouldConsumeMultipleResponse() throws JsonProcessingException {
        // given
        String exchangeId = "ARCX";

        int parts = 3;
        int part = 1;
        List<StockDto> responseDtos = new ArrayList<>();
        while (part <= parts) {
            StockMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(
                    this.generateRequestBody("MarketStackStockRequestNYSEARCA" + part + ".json"),
                    new TypeReference<StockMarketStackResponseWrapper>(){});
            responseDtos.addAll(responseWrapper.extract("USD"));
            ++part;
        }

        CacheControlDto cacheControlDto = CacheControlDto.builder()
                .endpointName("stocks.ARCX")
                .lastAccess(LocalDateTime.now().minusDays(2))
                .build();
        Mockito.when(cacheControlService.getCacheDataFor("stocks.ARCX")).thenReturn(cacheControlDto);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol("ARCX")
                .name("NYSE ARCA")
                .currency("USD")
                .build();
        Mockito.when(exchangeService.get(exchangeId)).thenReturn(exchangeDto);
        Mockito.when(externalRequests.makeMarketStackStocksRequest(any(MarketStackPagination.class), any(String.class)))
                .thenReturn(this.generateRequestBody("MarketStackStockRequestNYSEARCA1.json"))
                .thenReturn(this.generateRequestBody("MarketStackStockRequestNYSEARCA2.json"))
                .thenReturn(this.generateRequestBody("MarketStackStockRequestNYSEARCA3.json"));

        // when
        List<StockDto> result = controller.updateStocks(exchangeId);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for(int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getName()).isEqualTo(responseDtos.get(i).getName());
            Assertions.assertThat(result.get(i).getSymbol()).isEqualTo(responseDtos.get(i).getSymbol());
            Assertions.assertThat(result.get(i).getValue()).isEqualTo(responseDtos.get(i).getValue());
        }
    }

    private String generateRequestBody(String jsonName) {
        String requestBody = "";
        try {
            requestBody = StreamUtils.copyToString(
                    new ClassPathResource(jsonName).getInputStream(),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
