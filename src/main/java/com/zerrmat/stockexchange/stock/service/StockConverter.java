package com.zerrmat.stockexchange.stock.service;

import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import org.springframework.core.convert.ConversionException;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class StockConverter implements GenericConverter<StockModel, StockDto> {
    @Override
    public StockDto toDto(StockModel entity) throws ConversionException {
        MonetaryAmount value = Monetary.getDefaultAmountFactory().setCurrency(entity.getCurrency())
            .setNumber(entity.getValue()).create();

        return StockDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .value(value)
                .build();
    }

    @Override
    public StockModel toEntity(StockDto data) throws ConversionException {
        String currency = data.getValue().getCurrency().getCurrencyCode();
        BigDecimal value = data.getValue().getNumber().numberValue(BigDecimal.class);

        return StockModel.builder()
                .id(data.getId())
                .name(data.getName())
                .value(value)
                .currency(currency)
                .build();
    }
}
