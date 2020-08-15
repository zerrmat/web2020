package com.zerrmat.stockexchange.stockexchange.cachecontrol;

import com.zerrmat.stockexchange.cachecontrol.dao.CacheControlRepository;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.model.CacheControlModel;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlConverter;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class CacheControlServiceTest {
    private CacheControlService service;

    @Mock
    private CacheControlRepository repository;
    @Mock
    private CacheControlConverter converter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new CacheControlService(repository, converter);
    }

    @Test
    public void shouldReturnCacheForGivenEndpoint() {
        // given
        CacheControlModel model = CacheControlModel.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();
        CacheControlDto dto = CacheControlDto.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();

        Mockito.when(repository.getFirstByEndpointName("test")).thenReturn(model);
        Mockito.when(converter.toDto(model)).thenReturn(dto);

        // when
        CacheControlDto result = service.getCacheDataFor("test");

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEndpointName()).isEqualTo("test");
        Assertions.assertThat(result.getLastAccess()).isEqualTo(LocalDateTime.of(2020, 8, 15, 9, 55, 45));
    }

    @Test
    public void shouldUpdateOneEntity() {
        // given
        CacheControlModel model = CacheControlModel.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();
        CacheControlDto dto = CacheControlDto.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();

        Mockito.when(repository.getFirstByEndpointName("test")).thenReturn(model);

        // when
        boolean result = service.updateOne(dto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isTrue();
    }
}
