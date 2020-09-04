package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.rest.ExternalTickersController;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.stockexchange.util.RestTestUtils;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.ticker.marketstack.dto.TickerEODLatestMarketStackResponseWrapper;
import com.zerrmat.stockexchange.util.ZonedDateTimeDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class ExternalTickersControllerTest {
    @Mock
    private ExternalRequestsService externalRequestsService;
    @Mock
    private StockService stockService;
    @Mock
    private CacheControlService cacheControlService;

    private ExternalTickersController tickersController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tickersController = new ExternalTickersController(cacheControlService, externalRequestsService, stockService);
    }

    @Test
    public void shouldConsumeResponseWhenExchangeNull() throws JsonProcessingException {
        // given
        String responseFilename = "MarketStackTickersEODLatestRequest.json";
        String responseBody = RestTestUtils.generateResponseBody(responseFilename);
        String exchangeSymbol = "XWAR";
        String partStockSymbol = "CDR";
        String stockSymbol = "CDR.XWAR";

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        TickerEODLatestMarketStackResponseWrapper responseWrapper = objectMapper.readValue(
                responseBody, new TypeReference<TickerEODLatestMarketStackResponseWrapper>(){});
        TickerDto responseDto = responseWrapper.extract();

        MonetaryAmount value = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(responseDto.getClose()).create();
        StockDto stockDto = StockDto.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(value)
                .build();

        CacheControlDto cacheControlDto = CacheControlDto.builder()
                .endpointName("stocks." + exchangeSymbol + "." + partStockSymbol)
                .lastAccess(LocalDateTime.now().minusDays(2))
                .build();
        Mockito.when(cacheControlService.getCacheDataFor(cacheControlDto.getEndpointName()))
                .thenReturn(cacheControlDto);

        Mockito.when(externalRequestsService.makeExternalMarketStackTickersRequest(stockSymbol))
                .thenReturn(responseBody);
        Mockito.when(externalRequestsService.makeMarketStackTickersRequest(stockSymbol))
                .thenReturn(Collections.singletonList(responseDto));
        Mockito.when(stockService.updateStockValue(responseDto)).thenReturn(stockDto);

        // when
        List<StockDto> result = tickersController.executeEndpoint(stockSymbol);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo(stockDto.getName());
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo(stockDto.getSymbol());
        Assertions.assertThat(result.get(0).getValue()).isEqualTo(stockDto.getValue());
    }
}
