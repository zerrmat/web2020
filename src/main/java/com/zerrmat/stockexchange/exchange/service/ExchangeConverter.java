package com.zerrmat.stockexchange.exchange.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

@Component
public class ExchangeConverter implements GenericConverter<ExchangeModel, ExchangeDto> {
    @Override
    public ExchangeDto toDto(ExchangeModel entity) throws ConversionException {
        return ExchangeDto.builder()
                .id(entity.getId())
                .symbol(entity.getSymbol())
                .currency(entity.getCurrency())
                .name(entity.getName())
                .build();
    }

    @Override
    public ExchangeModel toEntity(ExchangeDto data) throws ConversionException {
        return ExchangeModel.builder()
                .id(data.getId())
                .symbol(data.getSymbol())
                .currency(data.getCurrency())
                .name(data.getName())
                .build();
    }
}
