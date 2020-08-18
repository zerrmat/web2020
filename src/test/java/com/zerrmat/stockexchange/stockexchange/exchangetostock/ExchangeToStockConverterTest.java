package com.zerrmat.stockexchange.stockexchange.exchangetostock;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockConverter;
import com.zerrmat.stockexchange.stock.model.StockModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class ExchangeToStockConverterTest {
    private ExchangeToStockConverter converter;

    @BeforeEach
    public void setup() {
        converter = new ExchangeToStockConverter();
    }

    @Test
    public void shouldConvertModelToDto() {
        // given
        ExchangeModel exchangeModel = ExchangeModel.builder()
                .id(1L)
                .symbol("XWAR")
                .name("Warsaw Stock Exchange")
                .currency("PLN")
                .build();

        StockModel stockModel = StockModel.builder()
                .id(1L)
                .name("CD Projekt")
                .symbol("CDR")
                .value(BigDecimal.valueOf(392.60))
                .currency("PLN")
                .build();

        ExchangeToStockModel model = ExchangeToStockModel.builder()
                .id(1L)
                .exchange(exchangeModel)
                .stock(stockModel)
                .build();

        // when
        ExchangeToStockDto result = converter.toDto(model);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getExchangeName()).isEqualTo("Warsaw Stock Exchange");
        Assertions.assertThat(result.getExchangeSymbol()).isEqualTo("XWAR");
        Assertions.assertThat(result.getStockName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.getStockSymbol()).isEqualTo("CDR");
        MonetaryAmount resultCDR = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        Assertions.assertThat(result.getStockValue().getNumber().compareTo(resultCDR.getNumber())).isEqualTo(0);
        Assertions.assertThat(result.getStockValue().getCurrency()).isEqualTo(resultCDR.getCurrency());
    }

    @Test
    public void shouldConvertDtoToModel() {
        // given
        MonetaryAmount stockValue = Monetary.getDefaultAmountFactory().setCurrency("PLN")
                .setNumber(BigDecimal.valueOf(392.60)).create();
        ExchangeToStockDto dto = ExchangeToStockDto.builder()
                .id(1L)
                .exchangeId(1L)
                .exchangeName("Warsaw Stock Exchange")
                .exchangeSymbol("XWAR")
                .stockId(1L)
                .stockName("CD Projekt")
                .stockSymbol("CDR")
                .stockValue(stockValue)
                .build();

        // when
        ExchangeToStockModel result = converter.toEntity(dto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);

        Assertions.assertThat(result.getExchange()).isNotNull();
        Assertions.assertThat(result.getExchange().getId()).isEqualTo(1L);
        Assertions.assertThat(result.getExchange().getName()).isEqualTo("Warsaw Stock Exchange");
        Assertions.assertThat(result.getExchange().getSymbol()).isEqualTo("XWAR");
        Assertions.assertThat(result.getExchange().getCurrency()).isEqualTo("PLN");

        Assertions.assertThat(result.getStock()).isNotNull();
        Assertions.assertThat(result.getStock().getId()).isEqualTo(1L);
        Assertions.assertThat(result.getStock().getName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.getStock().getSymbol()).isEqualTo("CDR");
        Assertions.assertThat(result.getStock().getValue()).isEqualTo(BigDecimal.valueOf(392.60));
        Assertions.assertThat(result.getStock().getCurrency()).isEqualTo("PLN");
    }
}
