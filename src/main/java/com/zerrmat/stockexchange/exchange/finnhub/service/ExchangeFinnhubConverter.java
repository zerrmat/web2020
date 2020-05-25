package com.zerrmat.stockexchange.exchange.finnhub.service;

import com.zerrmat.stockexchange.exchange.finnhub.dto.ExchangeFinnhubResponse;
import com.zerrmat.stockexchange.exchange.finnhub.model.ExchangeFinnhubModel;
import com.zerrmat.stockexchange.util.ResponseConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

@Component
public class ExchangeFinnhubConverter implements ResponseConverter<ExchangeFinnhubModel, ExchangeFinnhubResponse> {
    @Override
    public ExchangeFinnhubModel toEntity(ExchangeFinnhubResponse data) throws ConversionException {
        return ExchangeFinnhubModel.builder()
            .code(data.getCode())
            .currency(data.getCurrency())
            .name(data.getName())
            .build();
    }
}
