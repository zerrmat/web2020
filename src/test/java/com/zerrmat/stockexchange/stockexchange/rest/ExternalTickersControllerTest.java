package com.zerrmat.stockexchange.stockexchange.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zerrmat.stockexchange.rest.ExternalTickersController;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
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

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class ExternalTickersControllerTest {
    @Mock
    private ExternalRequestsService externalRequestsService;
    @Mock
    private StockService stockService;

    private ExternalTickersController tickersController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tickersController = new ExternalTickersController(null, externalRequestsService, stockService);
    }

    @Test
    public void shouldConsumeResponse() throws JsonProcessingException {
        // given
        String responseFilename = "MarketStackTickersEODLatestRequest.json";
        String responseBody = RestTestUtils.generateResponseBody(responseFilename);
        String stockSymbol = "CDR.XWAR";

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        TickerEODLatestMarketStackResponseWrapper responseWrapper = objectMapper.readValue(
                responseBody, new TypeReference<TickerEODLatestMarketStackResponseWrapper>(){});
        TickerDto responseDto = responseWrapper.extract();

        Mockito.when(externalRequestsService.makeExternalMarketStackTickersRequest(stockSymbol))
                .thenReturn(responseBody);
        Mockito.when(externalRequestsService.makeMarketStackTickersRequest(stockSymbol))
                .thenReturn(Collections.singletonList(responseDto));

        // when
        List<TickerDto> result = tickersController.executeEndpoint(stockSymbol);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getOpen().compareTo(BigDecimal.valueOf(438.5))).isEqualTo(0);
        Assertions.assertThat(result.get(0).getHigh().compareTo(BigDecimal.valueOf(447.8))).isEqualTo(0);
        Assertions.assertThat(result.get(0).getLow().compareTo(BigDecimal.valueOf(436.4))).isEqualTo(0);
        Assertions.assertThat(result.get(0).getClose().compareTo(BigDecimal.valueOf(440.7))).isEqualTo(0);
        Assertions.assertThat(result.get(0).getVolume()).isEqualTo(279982);
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo(stockSymbol);
        ZoneId zoneUTC = ZoneId.of("Etc/UTC");
        Assertions.assertThat(result.get(0).getDate()).isEqualTo(ZonedDateTime.of(2020, 9, 1, 0, 0, 0, 0, zoneUTC));
    }
}
