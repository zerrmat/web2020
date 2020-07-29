package com.zerrmat.stockexchange.exchange.marketstack.service;

import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.util.ResponseConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

@Component
public class ExchangeMarketStackConverter implements ResponseConverter<ExchangeModel, ExchangeMarketStackResponse> {

    @Override
    public ExchangeModel toEntity(ExchangeMarketStackResponse response) throws ConversionException {
        return ExchangeModel.builder()
                //.code(response.getMic())
                //.name(response.getName())
                .build();
    }
}
