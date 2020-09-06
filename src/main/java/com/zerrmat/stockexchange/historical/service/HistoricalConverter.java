package com.zerrmat.stockexchange.historical.service;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.model.HistoricalModel;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.util.GenericConverter;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Component
public class HistoricalConverter implements GenericConverter<HistoricalModel, HistoricalDto> {

    @Override
    public HistoricalDto toDto(HistoricalModel entity) throws ConversionException {
        MonetaryAmount value = Monetary.getDefaultAmountFactory()
                .setCurrency(entity.getEts().getStock().getCurrency())
                .setNumber(entity.getValue()).create();

        return HistoricalDto.builder()
                .etsId(entity.getEts().getId())
                .exchangeId(entity.getEts().getExchange().getId())
                .exchangeName(entity.getEts().getExchange().getName())
                .exchangeSymbol(entity.getEts().getExchange().getSymbol())
                .exchangeCurrency(entity.getEts().getExchange().getCurrency())
                .stockId(entity.getEts().getStock().getId())
                .stockName(entity.getEts().getStock().getName())
                .stockSymbol(entity.getEts().getStock().getSymbol())
                .value(value)
                .volume(entity.getVolume())
                .date(entity.getDate())
                .build();
    }

    @Override
    public HistoricalModel toEntity(HistoricalDto data) throws ConversionException {
        ExchangeModel exchangeModel = ExchangeModel.builder()
                .id(data.getExchangeId())
                .name(data.getExchangeName())
                .symbol(data.getExchangeSymbol())
                .currency(data.getExchangeCurrency())
                .build();

        StockModel stockModel = StockModel.builder()
                .id(data.getStockId())
                .name(data.getStockName())
                .symbol(data.getStockSymbol())
                .currency(data.getValue().getCurrency().getCurrencyCode())
                .build();

        ExchangeToStockModel etsModel = ExchangeToStockModel.builder()
                .id(data.getEtsId())
                .exchange(exchangeModel)
                .stock(stockModel)
                .build();

        return HistoricalModel.builder()
                .ets(etsModel)
                .value(data.getValue().getNumber().numberValue(BigDecimal.class))
                .volume(data.getVolume())
                .date(data.getDate())
                .build();
    }
}
