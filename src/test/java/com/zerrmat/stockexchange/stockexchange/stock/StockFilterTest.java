package com.zerrmat.stockexchange.stockexchange.stock;

import com.zerrmat.stockexchange.stock.dao.StockRepositoryFilter;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class StockFilterTest {
    private StockRepositoryFilter repositoryFilter;

    @BeforeEach
    public void setup() {
        repositoryFilter = new StockRepositoryFilter();
    }

    @Test
    public void shouldDeleteObsoleteValuesFromGivenSet() {
        // given
        StockDto dtoA = StockDto.builder().symbol("A").name("1").build();
        StockDto dtoB = StockDto.builder().symbol("B").name("2").build();
        StockDto dtoC = StockDto.builder().symbol("C").name("3").build();
        StockDto dtoD = StockDto.builder().symbol("D").name("4").build();

        List<StockDto> newSet = Arrays.asList(dtoA, dtoB);
        List<StockDto> actualSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);

        // when
        List<StockDto> result = repositoryFilter.getObsoleteStocks(newSet, actualSet);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo(dtoC.getSymbol());
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo(dtoD.getSymbol());
    }

    @Test
    public void shouldAddNewValuesToGivenSet() {
        // given
        StockDto dtoA = StockDto.builder().symbol("A").name("1").build();
        StockDto dtoB = StockDto.builder().symbol("B").name("2").build();
        StockDto dtoC = StockDto.builder().symbol("C").name("3").build();
        StockDto dtoD = StockDto.builder().symbol("D").name("4").build();

        List<StockDto> newSet = Arrays.asList(dtoA, dtoB, dtoC, dtoD);
        List<StockDto> actualSet = Arrays.asList(dtoA, dtoB);

        // when
        List<StockDto> result = repositoryFilter.getNewStocks(newSet, actualSet);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo(dtoC.getSymbol());
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo(dtoD.getSymbol());
    }
}
