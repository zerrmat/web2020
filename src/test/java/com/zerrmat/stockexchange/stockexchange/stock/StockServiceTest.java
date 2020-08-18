package com.zerrmat.stockexchange.stockexchange.stock;

import com.zerrmat.stockexchange.stock.dao.StockRepository;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import com.zerrmat.stockexchange.stock.service.StockService;
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

public class StockServiceTest {
    private StockService service;

    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockConverter converter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new StockService(stockRepository, converter);
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
        Mockito.when(stockRepository.findAll()).thenReturn(modelList);
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

        Mockito.when(stockRepository.getBySymbol("CDR")).thenReturn(model);
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


}
