package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.rest.ExternalHistoricalController;
import com.zerrmat.stockexchange.rest.ExternalStocksController;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.stockexchange.util.RestTestUtils;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.ticker.marketstack.dto.TickerHistoricalMarketStackResponseWrapper;
import com.zerrmat.stockexchange.util.ZonedDateTimeDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ExternalHistoricalControllerTest {
    private ExternalHistoricalController controller;

    @Mock
    private CacheControlService cacheControlService;
    @Mock
    private ExternalRequestsService externalRequestsService;
    @Mock
    private HistoricalService historicalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new ExternalHistoricalController(cacheControlService, externalRequestsService,
                historicalService);
    }

    @Test
    public void shouldConsumeResponse() throws JsonProcessingException {
        // given
        String exchangeSymbol = "XWAR";
        String stockSymbol = "CDR.XWAR";
        String responseFilename = "MarketStackTickerHistoricalRequest.json";
        String currency = "EUR";

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        String responseBody = RestTestUtils.generateResponseBody(responseFilename);
        TickerHistoricalMarketStackResponseWrapper responseWrapper = objectMapper.readValue(
                responseBody, new TypeReference<TickerHistoricalMarketStackResponseWrapper>(){});
        List<HistoricalDto> responseDtos = responseWrapper.extract(currency);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol(exchangeSymbol)
                .name("Warsaw Stock Exchange")
                .currency(currency)
                .build();
        LocalDate from = LocalDate.of(2020, 8, 24);
        LocalDate to = LocalDate.of(2020, 9, 3);

        Mockito.when(historicalService.getHistoricalDataForStock(exchangeDto.getSymbol(), stockSymbol))
                .thenReturn(new ArrayList<>());
        Mockito.when(externalRequestsService.makeExternalMarketStackHistoricalRequest(stockSymbol, from, to))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename));
        Mockito.when(externalRequestsService.makeMarketStackHistoricalRequest(exchangeDto.getCurrency(),
                stockSymbol, from, to)).thenReturn(responseDtos);

        // when
        List<HistoricalDto> result = controller.executeEndpoint(exchangeDto, stockSymbol, from, to);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for(int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getExchangeCurrency()).isEqualTo(responseDtos.get(i).getExchangeCurrency());
            Assertions.assertThat(result.get(i).getExchangeName()).isEqualTo(responseDtos.get(i).getExchangeName());
            Assertions.assertThat(result.get(i).getExchangeSymbol()).isEqualTo(responseDtos.get(i).getExchangeSymbol());
            Assertions.assertThat(result.get(i).getStockName()).isEqualTo(responseDtos.get(i).getStockName());
            Assertions.assertThat(result.get(i).getStockSymbol()).isEqualTo(responseDtos.get(i).getStockSymbol());
            Assertions.assertThat(result.get(i).getValue()).isEqualTo(result.get(i).getValue());
            Assertions.assertThat(result.get(i).getVolume()).isEqualTo(result.get(i).getVolume());
            Assertions.assertThat(result.get(i).getDate()).isEqualTo(result.get(i).getDate());
        }
    }

    @Test
    public void shouldConsumeMultipartResponse() throws JsonProcessingException {
        // given
        String exchangeSymbol = "XWAR";
        String stockSymbol = "CDR.XWAR";
        String responseFilename = "MarketStackTickerHistoricalMultipart";
        String currency = "EUR";

        List<HistoricalDto> responseDtos = this.generateMultipartResponse(3, responseFilename);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol(exchangeSymbol)
                .name("Warsaw Stock Exchange")
                .currency(currency)
                .build();
        LocalDate from = LocalDate.of(2019, 9, 3);
        LocalDate to = LocalDate.of(2020, 9, 3);

        Mockito.when(historicalService.getHistoricalDataForStock(exchangeDto.getSymbol(), stockSymbol))
                .thenReturn(new ArrayList<>());
        Mockito.when(externalRequestsService.makeExternalMarketStackHistoricalRequest(stockSymbol, from, to))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "1.json"))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "2.json"))
                .thenReturn(RestTestUtils.generateResponseBody(responseFilename + "3.json"));
        Mockito.when(externalRequestsService.makeMarketStackHistoricalRequest(exchangeDto.getCurrency(),
                stockSymbol, from, to)).thenReturn(responseDtos);

        // when
        List<HistoricalDto> result = controller.executeEndpoint(exchangeDto, stockSymbol, from, to);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(responseDtos.size());
        for(int i = 0; i < result.size(); i++) {
            Assertions.assertThat(result.get(i).getExchangeCurrency()).isEqualTo(responseDtos.get(i).getExchangeCurrency());
            Assertions.assertThat(result.get(i).getExchangeName()).isEqualTo(responseDtos.get(i).getExchangeName());
            Assertions.assertThat(result.get(i).getExchangeSymbol()).isEqualTo(responseDtos.get(i).getExchangeSymbol());
            Assertions.assertThat(result.get(i).getStockName()).isEqualTo(responseDtos.get(i).getStockName());
            Assertions.assertThat(result.get(i).getStockSymbol()).isEqualTo(responseDtos.get(i).getStockSymbol());
            Assertions.assertThat(result.get(i).getValue()).isEqualTo(result.get(i).getValue());
            Assertions.assertThat(result.get(i).getVolume()).isEqualTo(result.get(i).getVolume());
            Assertions.assertThat(result.get(i).getDate()).isEqualTo(result.get(i).getDate());
        }
    }

    private List<HistoricalDto> generateMultipartResponse(int parts, String responseFilename) throws JsonProcessingException {
        int actualPart = 1;
        List<HistoricalDto> result = new ArrayList<>();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        while (actualPart <= parts) {
            TickerHistoricalMarketStackResponseWrapper responseWrapper = objectMapper.readValue(
                    RestTestUtils.generateResponseBody(responseFilename + actualPart + ".json"),
                    new TypeReference<TickerHistoricalMarketStackResponseWrapper>(){});
            result.addAll(responseWrapper.extract("EUR"));
            ++actualPart;
        }

        return result;
    }
}
