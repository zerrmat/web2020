package com.zerrmat.stockexchange.stockexchange.exchange.finnhub;

import com.zerrmat.stockexchange.exchange.finnhub.dto.ExchangeFinnhubResponse;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchange.finnhub.service.ExchangeFinnhubConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExchangeFinnhubConverterTest {
    private ExchangeFinnhubConverter converter;

    @BeforeEach
    public void setup() {
        converter = new ExchangeFinnhubConverter();
    }

    @Test
    public void shouldConvertExchangeFinnhubResponseToModel() {
        // given
        ExchangeFinnhubResponse response = ExchangeFinnhubResponse.builder()
            .code("WA")
            .currency("PLN")
            .name("WARSAW STOCK EXCHANGE/EQUITIES/MAIN MARKET")
            .build();

        // when
        ExchangeModel model = converter.toEntity(response);

        // then
        Assertions.assertThat(model.getCode()).isEqualTo(response.getCode());
        Assertions.assertThat(model.getCurrency()).isEqualTo(response.getCurrency());
        Assertions.assertThat(model.getName()).isEqualTo(response.getName());
    }
}
