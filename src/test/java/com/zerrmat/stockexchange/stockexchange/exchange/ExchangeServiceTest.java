package com.zerrmat.stockexchange.stockexchange.exchange;

import com.zerrmat.stockexchange.exchange.dao.ExchangeRepository;
import com.zerrmat.stockexchange.exchange.dao.ExchangeRepositoryFilter;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchange.service.ExchangeConverter;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExchangeServiceTest {
    private ExchangeService service;

    @Mock
    private ExchangeConverter converter;
    @Mock
    private ExchangeRepository repository;
    @Mock
    private ExchangeRepositoryFilter repositoryFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ExchangeService(repository, converter, repositoryFilter);
    }

    @Test
    public void shouldReturnAllExchanges() {
        // given
        ExchangeModel em1 = ExchangeModel.builder().id(1L).symbol("ABC").build();
        ExchangeModel em2 = ExchangeModel.builder().id(2L).symbol("DEF").build();
        List<ExchangeModel> exchangeModels = Arrays.asList(em1, em2);
        Mockito.when(repository.findAll()).thenReturn(exchangeModels);

        ExchangeDto ed1 = ExchangeDto.builder().id(1L).symbol("ABC").build();
        ExchangeDto ed2 = ExchangeDto.builder().id(2L).symbol("DEF").build();
        List<ExchangeDto> exchangeDtos = Arrays.asList(ed1, ed2);
        Mockito.when(converter.convertAllToDto(exchangeModels)).thenReturn(exchangeDtos);

        // when
        List<ExchangeDto> result = service.getAll();

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result.get(0)).isNotNull();
        Assertions.assertThat(result.get(0).getClass()).isEqualTo(ExchangeDto.class);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo("ABC");

        Assertions.assertThat(result.get(1)).isNotNull();
        Assertions.assertThat(result.get(1).getClass()).isEqualTo(ExchangeDto.class);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo("DEF");
    }

    @Test
    public void shouldReturnExchangeBySymbol() {
        // given
        ExchangeModel em = ExchangeModel.builder().id(1L).symbol("ABC").build();
        Mockito.when(repository.findBySymbol("ABC")).thenReturn(em);

        ExchangeDto ed = ExchangeDto.builder().id(1L).symbol("ABC").build();
        Mockito.when(converter.toDto(em)).thenReturn(ed);

        // when
        ExchangeDto result = service.get("ABC");

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getClass()).isEqualTo(ExchangeDto.class);
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getSymbol()).isEqualTo("ABC");
    }

    @Test
    public void shouldUpdateExchanges() {
        // given
        ExchangeModel em1 = ExchangeModel.builder().id(1L).symbol("ABC").build();
        ExchangeModel em2 = ExchangeModel.builder().id(2L).symbol("DEF").build();
        List<ExchangeModel> exchangeModels = Arrays.asList(em1, em2);
        Mockito.when(repository.findAll()).thenReturn(exchangeModels);

        ExchangeDto ed1 = ExchangeDto.builder().id(1L).symbol("ABC").build();
        ExchangeDto ed2 = ExchangeDto.builder().id(2L).symbol("DEF").build();
        List<ExchangeDto> dbExchanges = Arrays.asList(ed1, ed2);
        Mockito.when(converter.convertAllToDto(exchangeModels)).thenReturn(dbExchanges);

        ExchangeDto ed3 = ExchangeDto.builder().id(3L).symbol("XYZ").build();
        List<ExchangeDto> actualExchanges = Arrays.asList(ed1, ed3);

        Mockito.when(repositoryFilter.getObsoleteExchanges(actualExchanges, dbExchanges))
                .thenReturn(Collections.singletonList(ed2));
        Mockito.when(repositoryFilter.getNewExchanges(actualExchanges, dbExchanges))
                .thenReturn(Collections.singletonList(ed3));

        // when
        boolean result = service.updateExchanges(actualExchanges);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void shouldNotSaveNewExchangesWhenConversionExceptionIsThrown() {
        // given
        ExchangeModel em1 = ExchangeModel.builder().id(1L).symbol("ABC").build();
        ExchangeModel em2 = ExchangeModel.builder().id(2L).symbol("DEF").build();
        List<ExchangeModel> exchangeModels = Arrays.asList(em1, em2);
        Mockito.when(repository.findAll()).thenReturn(exchangeModels);

        ExchangeDto ed1 = ExchangeDto.builder().id(1L).symbol("ABC").build();
        ExchangeDto ed2 = ExchangeDto.builder().id(2L).symbol("DEF").build();
        List<ExchangeDto> dbExchanges = Arrays.asList(ed1, ed2);
        Mockito.when(converter.convertAllToDto(exchangeModels)).thenReturn(dbExchanges);

        ExchangeDto ed3 = ExchangeDto.builder().id(3L).symbol("XYZ").build();
        List<ExchangeDto> actualExchanges = Arrays.asList(ed1, ed3);

        Mockito.when(repositoryFilter.getObsoleteExchanges(actualExchanges, dbExchanges))
                .thenReturn(Collections.singletonList(ed2));

        List<ExchangeDto> newExchanges = Collections.singletonList(ed3);
        Mockito.when(repositoryFilter.getNewExchanges(actualExchanges, dbExchanges))
                .thenReturn(newExchanges);

        Mockito.doThrow(new ConversionException("fail") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(converter).convertAllToEntity(newExchanges);

        // when
        boolean result = service.updateExchanges(actualExchanges);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isFalse();
    }
}
