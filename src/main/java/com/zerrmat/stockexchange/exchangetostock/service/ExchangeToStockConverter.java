package com.zerrmat.stockexchange.exchangetostock.service;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Component
public class ExchangeToStockConverter implements GenericConverter<ExchangeToStockModel, ExchangeToStockDto> {
    @Override
    public ExchangeToStockDto toDto(ExchangeToStockModel entity) throws ConversionException {
        MonetaryAmount stockValue = Monetary.getDefaultAmountFactory()
                .setCurrency(entity.getStock().getCurrency())
                .setNumber(entity.getStock().getValue()).create();

        return ExchangeToStockDto.builder()
                .id(entity.getId())
                .exchangeId(entity.getExchange().getId())
                .exchangeName(entity.getExchange().getName())
                .exchangeSymbol(entity.getExchange().getSymbol())
                .stockId(entity.getStock().getId())
                .stockName(entity.getStock().getName())
                .stockSymbol(entity.getStock().getSymbol())
                .stockValue(stockValue)
                .build();
    }

    @Override
    public ExchangeToStockModel toEntity(ExchangeToStockDto data) throws ConversionException {
        ExchangeModel exchangeModel = ExchangeModel.builder()
                .id(data.getExchangeId())
                .name(data.getExchangeName())
                .symbol(data.getExchangeSymbol())
                .currency(data.getStockValue().getCurrency().toString())
                .build();

        StockModel stockModel = StockModel.builder()
                .id(data.getStockId())
                .name(data.getStockName())
                .symbol(data.getStockSymbol())
                .value(new BigDecimal(data.getStockValue().getNumber().toString()))
                .currency(data.getStockValue().getCurrency().toString())
                .build();

        return ExchangeToStockModel.builder()
                .id(data.getId())
                .exchange(exchangeModel)
                .stock(stockModel)
                .build();
    }
}
