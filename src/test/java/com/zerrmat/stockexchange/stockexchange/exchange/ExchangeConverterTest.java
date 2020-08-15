package com.zerrmat.stockexchange.stockexchange.exchange;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchange.service.ExchangeConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExchangeConverterTest {
    private ExchangeConverter converter;

    @BeforeEach
    public void setup() {
        converter = new ExchangeConverter();
    }

    @Test
    public void shouldConvertModelToDto() {
        // given
        ExchangeModel exchangeModel = ExchangeModel.builder()
                .id(1L)
                .code("ABC")
                .name("ABC Exchange")
                .currency("USD")
                .build();

        // when
        ExchangeDto result = converter.toDto(exchangeModel);

        // then
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getCode()).isEqualTo("ABC");
        Assertions.assertThat(result.getName()).isEqualTo("ABC Exchange");
        Assertions.assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    public void shouldConvertDtoToModel() {
        // given
        ExchangeDto exchangeDto = ExchangeDto.builder()
                .id(1L)
                .code("ABC")
                .name("ABC Exchange")
                .currency("USD")
                .build();

        // when
        ExchangeModel result = converter.toEntity(exchangeDto);

        // then
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getCode()).isEqualTo("ABC");
        Assertions.assertThat(result.getName()).isEqualTo("ABC Exchange");
        Assertions.assertThat(result.getCurrency()).isEqualTo("USD");
    }
}
