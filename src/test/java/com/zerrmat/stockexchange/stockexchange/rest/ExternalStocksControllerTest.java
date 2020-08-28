package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.rest.ExternalStocksController;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.stockexchange.util.RestTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ExternalStocksControllerTest {
    private ExternalStocksController controller;

    @Mock
    private ExchangeService exchangeService;
    @Mock
    private StockService stockService;
    @Mock
    private CacheControlService cacheControlService;
    @Mock
    private ExternalRequestsService externalRequestsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new ExternalStocksController(cacheControlService, externalRequestsService, exchangeService, stockService);
    }

    @Test
    public void shouldConsumeResponse() throws JsonProcessingException {
        // given
        String exchangeSymbol = "XWAR";
        String responseFilename = "MarketStackStockRequestWarsaw.json";
        String responseBody = RestTestUtils.generateResponseBody(responseFilename);

        StockMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(
                responseBody, new TypeReference<StockMarketStackResponseWrapper>(){});
        List<StockDto> responseDtos = responseWrapper.extract("EUR");
        CacheControlDto cacheControlDto = CacheControlDto.builder()
                .endpointName("stocks." + exchangeSymbol)
                .lastAccess(LocalDateTime.now().minusDays(2))
                .build();
        Mockito.when(cacheControlService.getCacheDataFor("stocks." + exchangeSymbol))
                .thenReturn(cacheControlDto);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol(exchangeSymbol)
                .name("Warsaw Stock Exchange")
                .currency("EUR")
                .build();
        Mockito.when(exchangeService.getBySymbol(exchangeSymbol)).thenReturn(exchangeDto);

        Mockito.when(externalRequestsService.makeExternalMarketStackStocksRequest(
                any(MarketStackPagination.class), any(String.class))).thenReturn(responseBody);
        Mockito.when(externalRequestsService.makeMarketStackStocksRequest(any(String.class), eq(exchangeService)))
                .thenReturn(responseDtos);

        // when
        List<StockDto> result = controller.executeEndpoint(exchangeSymbol);

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
    public void shouldConsumeMultipartResponse() throws JsonProcessingException {
        // given
        String exchangeSymbol = "ARCX";
        String responseFilename = "MarketStackStockRequestNYSEARCA";

        List<StockDto> responseDtos = this.generateMultipartResponse(3, responseFilename);

        CacheControlDto cacheControlDto = CacheControlDto.builder()
                .endpointName("stocks." + exchangeSymbol)
                .lastAccess(LocalDateTime.now().minusDays(2))
                .build();
        Mockito.when(cacheControlService.getCacheDataFor("stocks." + exchangeSymbol))
                .thenReturn(cacheControlDto);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol(exchangeSymbol)
                .name("NYSE ARCA")
                .currency("USD")
                .build();
        Mockito.when(exchangeService.getBySymbol(exchangeSymbol)).thenReturn(exchangeDto);
        Mockito.when(externalRequestsService.makeExternalMarketStackStocksRequest(any(MarketStackPagination.class), any(String.class)))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "1.json"))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "2.json"))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "3.json"));
        Mockito.when(externalRequestsService.makeMarketStackStocksRequest(any(String.class), eq(exchangeService)))
                .thenReturn(responseDtos);

        // when
        List<StockDto> result = controller.executeEndpoint(exchangeSymbol);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for(int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getName()).isEqualTo(responseDtos.get(i).getName());
            Assertions.assertThat(result.get(i).getSymbol()).isEqualTo(responseDtos.get(i).getSymbol());
            Assertions.assertThat(result.get(i).getValue()).isEqualTo(responseDtos.get(i).getValue());
        }
    }

    private List<StockDto> generateMultipartResponse(int parts, String responseFilename) throws JsonProcessingException {
        int actualPart = 1;
        List<StockDto> result = new ArrayList<>();

        while (actualPart <= parts) {
            StockMarketStackResponseWrapper responseWrapper = new ObjectMapper().readValue(
                    RestTestUtils.generateResponseBody(responseFilename + actualPart + ".json"),
                    new TypeReference<StockMarketStackResponseWrapper>(){});
            result.addAll(responseWrapper.extract("USD"));
            ++actualPart;
        }

        return result;
    }
}
