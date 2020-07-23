package com.zerrmat.stockexchange.exchange.finnhub.service;

import com.zerrmat.stockexchange.exchange.finnhub.dto.ExchangeFinnhubResponse;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.util.ResponseConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExchangeFinnhubConverter implements ResponseConverter<ExchangeModel, ExchangeFinnhubResponse> {
    @Override
    public ExchangeModel toEntity(ExchangeFinnhubResponse data) throws ConversionException {
        return ExchangeModel.builder()
            .code(data.getCode())
            .currency(data.getCurrency())
            .name(data.getName())
            .build();
    }
}
