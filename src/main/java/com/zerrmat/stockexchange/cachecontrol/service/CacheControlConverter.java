package com.zerrmat.stockexchange.cachecontrol.service;

import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.model.CacheControlModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;

@Component
public class CacheControlConverter implements GenericConverter<CacheControlModel, CacheControlDto> {
    @Override
    public CacheControlDto toDto(CacheControlModel entity) throws ConversionException {
        return CacheControlDto.builder()
                .endpointName(entity.getEndpointName())
                .lastAccess(entity.getLastAccess())
                .build();
    }

    @Override
    public CacheControlModel toEntity(CacheControlDto data) throws ConversionException {
        return CacheControlModel.builder()
                .endpointName(data.getEndpointName())
                .lastAccess(data.getLastAccess())
                .build();
    }
}
