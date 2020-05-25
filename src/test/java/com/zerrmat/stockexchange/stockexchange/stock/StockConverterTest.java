package com.zerrmat.stockexchange.stockexchange.stock;

import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class StockConverterTest {
    private StockConverter stockConverter;

    @BeforeEach
    public void setup() {
        stockConverter = new StockConverter();
    }

    @Test
    public void shouldConvertStockModelToStockDto() {
        // given
        StockModel stockModel = StockModel.builder()
            .id(1L)
            .name("CDR")
            .value(BigDecimal.valueOf(392.60))
            .currency("PLN")
            .build();

        // when
        StockDto stockDto = stockConverter.toDto(stockModel);

        // then
        Assertions.assertThat(stockDto.getId()).isEqualTo(1L);
        Assertions.assertThat(stockDto.getName()).isEqualTo("CDR");
        MonetaryAmount value = Monetary.getDefaultAmountFactory().setCurrency("PLN")
            .setNumber(BigDecimal.valueOf(392.60)).create();
        Assertions.assertThat(stockDto.getValue().getNumber().compareTo(value.getNumber())).isEqualTo(0);
        Assertions.assertThat(stockDto.getValue().getCurrency()).isEqualTo(value.getCurrency());
    }

    @Test
    public void shouldConvertStockDtoToStockModel() {
        // given
        MonetaryAmount value = Monetary.getDefaultAmountFactory().setCurrency("PLN")
            .setNumber(BigDecimal.valueOf(392.60)).create();
        StockDto stockDto = StockDto.builder()
            .id(1L)
            .name("CDR")
            .value(value)
            .build();

        // when
        StockModel stockModel = stockConverter.toEntity(stockDto);

        // then
        Assertions.assertThat(stockModel.getId()).isEqualTo(stockDto.getId());
        Assertions.assertThat(stockModel.getName()).isEqualTo(stockDto.getName());
        BigDecimal numberValue = stockDto.getValue().getNumber().numberValue(BigDecimal.class);
        Assertions.assertThat(stockModel.getValue().compareTo(numberValue)).isEqualTo(0);
        String currencyCode = stockDto.getValue().getCurrency().getCurrencyCode();
        Assertions.assertThat(stockModel.getCurrency().equals(currencyCode)).isTrue();
    }
}
