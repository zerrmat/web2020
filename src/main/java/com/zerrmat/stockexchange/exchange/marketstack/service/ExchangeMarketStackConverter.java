package com.zerrmat.stockexchange.exchange.marketstack.service;

import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import com.zerrmat.stockexchange.util.ResponseConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

@Component
public class ExchangeMarketStackConverter implements GenericConverter<ExchangeModel, ExchangeMarketStackResponse> {

    @Override
    public ExchangeMarketStackResponse toDto(ExchangeModel entity) throws ConversionException {
        return ExchangeMarketStackResponse.builder()
                .code(entity.getCode())
                .currency(entity.getCurrency())
                .name(entity.getName())
                .build();
    }

    @Override
    public ExchangeModel toEntity(ExchangeMarketStackResponse response) throws ConversionException {
        return ExchangeModel.builder()
                .code(response.getCode())
                .name(response.getName())
                .currency(response.getCurrency())
                .build();
    }
}
