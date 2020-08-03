package com.zerrmat.stockexchange.stockexchange.exchange.marketstack;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepository;
import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepositoryFilter;
import com.zerrmat.stockexchange.exchange.marketstack.service.ExchangeMarketStackService;
import com.zerrmat.stockexchange.exchange.service.ExchangeConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class ExchangeMarketStackServiceTest {
    @Test
    public void shouldDeleteObsoleteValuesFromGivenSet() {
        // given
        ExchangeDto dtoA = ExchangeDto.builder().code("A").name("1").currency("USD").build();
        ExchangeDto dtoB = ExchangeDto.builder().code("B").name("2").currency("EUR").build();
        ExchangeDto dtoC = ExchangeDto.builder().code("C").name("3").currency("CHF").build();
        ExchangeDto dtoD = ExchangeDto.builder().code("D").name("4").currency("GBP").build();
        List<ExchangeDto> newSet = Arrays.asList(dtoA, dtoB);
        List<ExchangeDto> actualSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);

        // when
        List<ExchangeDto> resultSet = ExchangeMarketStackRepositoryFilter.getObsoleteExchanges(newSet, actualSet);

        // then
        Assertions.assertThat(resultSet.size()).isEqualTo(2);
        Assertions.assertThat(resultSet.get(0).getCode()).isEqualTo(dtoC.getCode());
        Assertions.assertThat(resultSet.get(1).getCode()).isEqualTo(dtoD.getCode());
    }

    @Test
    public void shouldAddNewValuesToGivenSet() {
        // given
        ExchangeDto dtoA = ExchangeDto.builder().code("A").name("1").currency("USD").build();
        ExchangeDto dtoB = ExchangeDto.builder().code("B").name("2").currency("EUR").build();
        ExchangeDto dtoC = ExchangeDto.builder().code("C").name("3").currency("CHF").build();
        ExchangeDto dtoD = ExchangeDto.builder().code("D").name("4").currency("GBP").build();
        List<ExchangeDto> newSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);
        List<ExchangeDto> actualSet = Arrays.asList(dtoA, dtoB);

        // when
        List<ExchangeDto> resultSet = ExchangeMarketStackRepositoryFilter.getNewExchanges(newSet, actualSet);

        // then
        Assertions.assertThat(resultSet.size()).isEqualTo(2);
        Assertions.assertThat(resultSet.get(0).getCode()).isEqualTo(dtoC.getCode());
        Assertions.assertThat(resultSet.get(1).getCode()).isEqualTo(dtoD.getCode());
    }
}
