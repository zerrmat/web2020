package com.zerrmat.stockexchange.stockexchange.exchange;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.dao.ExchangeRepositoryFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ExchangeRepositoryFilterTest {
    private ExchangeRepositoryFilter repositoryFilter;

    @BeforeEach
    public void setup() {
        repositoryFilter = new ExchangeRepositoryFilter();
    }

    @Test
    public void shouldDeleteObsoleteValuesFromGivenSet() {
        // given
        ExchangeDto dtoA = ExchangeDto.builder().symbol("A").name("1").currency("USD").build();
        ExchangeDto dtoB = ExchangeDto.builder().symbol("B").name("2").currency("EUR").build();
        ExchangeDto dtoC = ExchangeDto.builder().symbol("C").name("3").currency("CHF").build();
        ExchangeDto dtoD = ExchangeDto.builder().symbol("D").name("4").currency("GBP").build();
        List<ExchangeDto> newSet = Arrays.asList(dtoA, dtoB);
        List<ExchangeDto> actualSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);

        // when
        List<ExchangeDto> resultSet = repositoryFilter.getObsoleteExchanges(newSet, actualSet);

        // then
        Assertions.assertThat(resultSet.size()).isEqualTo(2);
        Assertions.assertThat(resultSet.get(0).getSymbol()).isEqualTo(dtoC.getSymbol());
        Assertions.assertThat(resultSet.get(1).getSymbol()).isEqualTo(dtoD.getSymbol());
    }

    @Test
    public void shouldAddNewValuesToGivenSet() {
        // given
        ExchangeDto dtoA = ExchangeDto.builder().symbol("A").name("1").currency("USD").build();
        ExchangeDto dtoB = ExchangeDto.builder().symbol("B").name("2").currency("EUR").build();
        ExchangeDto dtoC = ExchangeDto.builder().symbol("C").name("3").currency("CHF").build();
        ExchangeDto dtoD = ExchangeDto.builder().symbol("D").name("4").currency("GBP").build();
        List<ExchangeDto> newSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);
        List<ExchangeDto> actualSet = Arrays.asList(dtoA, dtoB);

        // when
        List<ExchangeDto> resultSet = repositoryFilter.getNewExchanges(newSet, actualSet);

        // then
        Assertions.assertThat(resultSet.size()).isEqualTo(2);
        Assertions.assertThat(resultSet.get(0).getSymbol()).isEqualTo(dtoC.getSymbol());
        Assertions.assertThat(resultSet.get(1).getSymbol()).isEqualTo(dtoD.getSymbol());
    }
}
