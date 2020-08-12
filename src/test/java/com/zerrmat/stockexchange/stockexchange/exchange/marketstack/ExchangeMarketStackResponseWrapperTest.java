package com.zerrmat.stockexchange.stockexchange.exchange.marketstack;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponseWrapper;
import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackCurrency;
import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackData;
import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackPagination;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ExchangeMarketStackResponseWrapperTest {
    private ExchangeMarketStackResponseWrapper wrapper;

    @Test
    public void shouldExtractData() {
        // given
        MarketStackPagination pagination = MarketStackPagination.builder()
                .limit(100).offset(0).count(2).total(2).build();
        MarketStackCurrency currency = MarketStackCurrency.builder().code("USD").build();

        MarketStackData data1 = MarketStackData.builder()
                .name("ABC Exchange").mic("ABC").currency(currency).build();
        MarketStackData data2 = MarketStackData.builder()
                .name("XYZ Stock Market").mic("XYZS").currency(currency).build();
        List<MarketStackData> data = Arrays.asList(data1, data2);

        wrapper = ExchangeMarketStackResponseWrapper.builder()
                .pagination(pagination)
                .data(data)
                .build();

        // when
        List<ExchangeDto> result = wrapper.extract();

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result.get(0)).isInstanceOf(ExchangeDto.class);
        Assertions.assertThat(result.get(0).getCode()).isEqualTo("ABC");
        Assertions.assertThat(result.get(0).getName()).isEqualTo("ABC Exchange");
        Assertions.assertThat(result.get(0).getCurrency()).isEqualTo("USD");

        Assertions.assertThat(result.get(1)).isInstanceOf(ExchangeDto.class);
        Assertions.assertThat(result.get(1).getCode()).isEqualTo("XYZS");
        Assertions.assertThat(result.get(1).getName()).isEqualTo("XYZ Stock Market");
        Assertions.assertThat(result.get(1).getCurrency()).isEqualTo("USD");
    }
}
