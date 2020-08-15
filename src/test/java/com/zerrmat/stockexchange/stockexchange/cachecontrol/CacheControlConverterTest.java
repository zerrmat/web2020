package com.zerrmat.stockexchange.stockexchange.cachecontrol;

import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.model.CacheControlModel;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CacheControlConverterTest {
    private CacheControlConverter converter;

    @BeforeEach
    public void setup() {
        converter = new CacheControlConverter();
    }

    @Test
    public void shouldConvertFromModelToDto() {
        // given
        CacheControlModel model = CacheControlModel.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();

        // when
        CacheControlDto result = converter.toDto(model);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEndpointName()).isEqualTo("test");
        Assertions.assertThat(result.getLastAccess()).isEqualTo(LocalDateTime.of(2020, 8, 15, 9, 55, 45));
    }

    @Test
    public void shouldConvertFromDtoToModel() {
        // given
        CacheControlDto dto = CacheControlDto.builder()
                .endpointName("test")
                .lastAccess(LocalDateTime.of(2020, 8, 15, 9, 55, 45))
                .build();

        // when
        CacheControlModel result = converter.toEntity(dto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEndpointName()).isEqualTo("test");
        Assertions.assertThat(result.getLastAccess()).isEqualTo(LocalDateTime.of(2020, 8, 15, 9, 55, 45));
    }
}
