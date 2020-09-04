package com.zerrmat.stockexchange.stockexchange.stock;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.stock.dao.StockRepository;
import com.zerrmat.stockexchange.stock.dao.StockRepositoryFilter;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class StockServiceTest {
    private StockService service;

    @Mock
    private StockRepository repository;
    @Mock
    private StockConverter converter;
    @Mock
    private StockRepositoryFilter repositoryFilter;
    @Mock
    private ExchangeToStockService etsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new StockService(repository, converter, repositoryFilter, etsService);
    }

    @Test
    public void shouldGetAllStocks() {
        // given
        StockModel sm1 = StockModel.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(BigDecimal.valueOf(392.60))
                .currency("PLN")
                .build();
        StockModel sm2 = StockModel.builder()
                .id(2L)
                .name("Apple")
                .symbol("AAPL")
                .value(BigDecimal.valueOf(459.63))
                .currency("USD")
                .build();

        MonetaryAmount cdrMonetary = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        StockDto sd1 = StockDto.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(cdrMonetary)
                .build();

        MonetaryAmount appleMonetary = Monetary.getDefaultAmountFactory().setCurrency("USD")
                .setNumber(BigDecimal.valueOf(459.63)).create();
        StockDto sd2 = StockDto.builder()
                .id(2L)
                .name("Apple")
                .symbol("AAPL")
                .value(appleMonetary)
                .build();

        List<StockModel> modelList = Arrays.asList(sm1, sm2);
        Mockito.when(repository.findAll()).thenReturn(modelList);
        Mockito.when(converter.convertAllToDto(modelList)).thenReturn(Arrays.asList(sd1, sd2));

        // when
        List<StockDto> result = service.getAll();

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result.get(0)).isInstanceOf(StockDto.class);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo("CDR");
        MonetaryAmount resultCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        Assertions.assertThat(result.get(0).getValue().getNumber().compareTo(resultCDR.getNumber())).isEqualTo(0);
        Assertions.assertThat(result.get(0).getValue().getCurrency()).isEqualTo(resultCDR.getCurrency());

        Assertions.assertThat(result.get(1)).isInstanceOf(StockDto.class);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getName()).isEqualTo("Apple");
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo("AAPL");
        MonetaryAmount resultApple = Monetary.getDefaultAmountFactory().setCurrency("USD")
                .setNumber(BigDecimal.valueOf(459.63)).create();
        Assertions.assertThat(result.get(1).getValue().getNumber().compareTo(resultApple.getNumber())).isEqualTo(0);
        Assertions.assertThat(result.get(1).getValue().getCurrency()).isEqualTo(resultApple.getCurrency());
    }

    @Test
    public void shouldGetStockWithGivenSymbol() {
        // given
        StockModel model = StockModel.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(BigDecimal.valueOf(392.60))
                .currency("PLN")
                .build();

        MonetaryAmount cdrMonetary = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        StockDto dto = StockDto.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(cdrMonetary)
                .build();

        Mockito.when(repository.getBySymbol("CDR")).thenReturn(model);
        Mockito.when(converter.toDto(model)).thenReturn(dto);

        // when
        StockDto result = service.getBySymbol("CDR");

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.getSymbol()).isEqualTo("CDR");
        MonetaryAmount resultCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        Assertions.assertThat(result.getValue().getNumber().compareTo(resultCDR.getNumber())).isEqualTo(0);
        Assertions.assertThat(result.getValue().getCurrency()).isEqualTo(resultCDR.getCurrency());
    }

    @Test
    public void shouldUpdateStocks() {
        // given
        StockModel sm1 = StockModel.builder().id(1L).symbol("ABC").build();
        StockModel sm2 = StockModel.builder().id(1L).symbol("DEF").build();
        List<StockModel> stockModels = Arrays.asList(sm1, sm2);
        Mockito.when(repository.findAll()).thenReturn(stockModels);

        StockDto sd1 = StockDto.builder().id(1L).symbol("ABC").build();
        StockDto sd2 = StockDto.builder().id(1L).symbol("DEF").build();
        List<StockDto> dbStocks = Arrays.asList(sd1, sd2);
        Mockito.when(converter.convertAllToDto(stockModels)).thenReturn(dbStocks);

        StockDto sd3 = StockDto.builder().id(1L).symbol("XYZ").build();
        List<StockDto> actualStocks = Arrays.asList(sd1, sd3);

        Mockito.when(repositoryFilter.getObsoleteStocks(actualStocks, dbStocks))
                .thenReturn(Collections.singletonList(sd2));
        Mockito.when(repositoryFilter.getNewStocks(actualStocks, dbStocks))
                .thenReturn(Collections.singletonList(sd3));

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .symbol("XWAR")
                .name("Warsaw Stock Exchange")
                .currency("EUR")
                .build();

        // when
        boolean result = service.updateStocks(actualStocks, exchangeDto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void shouldUpdateValue() {
        // given
        TickerDto tickerDto = TickerDto.builder()
                .open(BigDecimal.valueOf(438.5))
                .high(BigDecimal.valueOf(447.8))
                .low(BigDecimal.valueOf(436.4))
                .close(BigDecimal.valueOf(440.7))
                .volume(279982L)
                .symbol("CDR.XWAR")
                .date(ZonedDateTime.of(2020, 9, 1, 0, 0, 0, 0, ZoneId.of("Etc/UTC")))
                .build();

        MonetaryAmount value = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(tickerDto.getClose()).create();
        StockDto stockDto = StockDto.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(value)
                .build();
        StockModel stockModel = StockModel.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(value.getNumber().numberValue(BigDecimal.class))
                .currency("PLN")
                .build();

        Mockito.when(repository.getBySymbol(tickerDto.getSymbol())).thenReturn(stockModel);
        Mockito.when(repository.save(stockModel)).thenReturn(stockModel);
        Mockito.when(converter.toDto(stockModel)).thenReturn(stockDto);

        // when
        StockDto result = service.updateStockValue(tickerDto);

        // then
        Assertions.assertThat(result.getName()).isEqualTo("CD Projekt");

        MonetaryAmount resultCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(440.7)).create();
        BigDecimal bigDecimal = resultCDR.getNumber().numberValue(BigDecimal.class);
        Assertions.assertThat(result.getValue().getNumber().numberValue(BigDecimal.class)
                .compareTo(bigDecimal)).isEqualTo(0);

        Assertions.assertThat(result.getSymbol()).isEqualTo("CDR");
    }
}
