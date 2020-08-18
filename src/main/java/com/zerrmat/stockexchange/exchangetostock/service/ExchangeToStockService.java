package com.zerrmat.stockexchange.exchangetostock.service;

import com.zerrmat.stockexchange.exchangetostock.dao.ExchangeToStockRepository;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeToStockService {
    private ExchangeToStockRepository repository;
    private ExchangeToStockConverter converter;
    private StockConverter stockConverter;

    public ExchangeToStockService(ExchangeToStockRepository repository, ExchangeToStockConverter converter,
                                  StockConverter stockConverter) {
        this.repository = repository;
        this.converter = converter;
        this.stockConverter = stockConverter;
    }

    public List<ExchangeToStockDto> getAll() {
        List<ExchangeToStockModel> models = repository.findAll();
        return converter.convertAllToDto(models);
    }

    public List<StockDto> getStocksForExchange(String exchangeCode) {
        List<ExchangeToStockModel> models = repository.findAllByExchange_Symbol(exchangeCode);
        return models.stream()
                .map(d -> stockConverter.toDto(d.getStock()))
                .collect(Collectors.toList());
    }
}
