package com.zerrmat.stockexchange.stockexchange.exchangetostock;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchangetostock.dao.ExchangeToStockRepository;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockConverter;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ExchangeToStockServiceTest {
    @Mock
    private ExchangeToStockRepository repository;
    @Mock
    private ExchangeToStockConverter converter;
    @Mock
    private StockConverter stockConverter;

    private ExchangeToStockService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ExchangeToStockService(repository, converter, stockConverter);
    }

    @Test
    public void shouldGetStocksForGivenExchangeCode() {
        // given
        ExchangeModel exchangeModel = ExchangeModel.builder()
                .id(1L)
                .symbol("XWAR")
                .name("Warsaw Stock Exchange")
                .currency("PLN")
                .build();
        StockModel sm1 = StockModel.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(BigDecimal.valueOf(392.60))
                .currency("PLN")
                .build();
        StockModel sm2 = StockModel.builder()
                .id(2L)
                .name(("11 bit studios"))
                .symbol("11B")
                .value(BigDecimal.valueOf(507.00))
                .currency("PLN")
                .build();

        ExchangeToStockModel etsm1 = ExchangeToStockModel.builder()
                .id(1L)
                .exchange(exchangeModel)
                .stock(sm1)
                .build();
        ExchangeToStockModel etsm2 = ExchangeToStockModel.builder()
                .id(1L)
                .exchange(exchangeModel)
                .stock(sm2)
                .build();
        List<ExchangeToStockModel> etsms = Arrays.asList(etsm1, etsm2);

        MonetaryAmount stockValueCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        StockDto etsd1 = StockDto.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(stockValueCDR)
                .build();
        MonetaryAmount stockValue11B = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(507.00)).create();
        StockDto etsd2 = StockDto.builder()
                .id(2L)
                .name("11 bit studios")
                .symbol("11B")
                .value(stockValue11B)
                .build();

        Long exchangeCode = exchangeModel.getId();

        Mockito.when(repository.findAllByExchange_Id(exchangeCode))
                .thenReturn(etsms);
        Mockito.when(stockConverter.toDto(etsms.get(0).getStock())).thenReturn(etsd1);
        Mockito.when(stockConverter.toDto(etsms.get(1).getStock())).thenReturn(etsd2);

        // when
        List<StockDto> result = service.getStocksForExchange(exchangeCode);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result.get(0)).isInstanceOf(StockDto.class);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo("CDR");
        MonetaryAmount resultCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        Assertions.assertThat(result.get(0).getValue().getNumber()
                .compareTo(resultCDR.getNumber())).isEqualTo(0);

        Assertions.assertThat(result.get(1)).isInstanceOf(StockDto.class);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getName()).isEqualTo("11 bit studios");
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo("11B");
        MonetaryAmount result11B = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(507.00)).create();
        Assertions.assertThat(result.get(1).getValue().getNumber()
                .compareTo(result11B.getNumber())).isEqualTo(0);
    }
}
