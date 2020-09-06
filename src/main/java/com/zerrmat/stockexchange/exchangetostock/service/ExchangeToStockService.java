package com.zerrmat.stockexchange.exchangetostock.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchangetostock.dao.ExchangeToStockRepository;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockConverter;
import org.springframework.core.convert.ConversionException;
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

    public ExchangeToStockDto getOne(String exchangeSymbol, String stockSymbol) {
        ExchangeToStockModel one = repository.findAllByExchange_SymbolAndStock_Symbol(exchangeSymbol, stockSymbol);
        return converter.toDto(one);
    }

    public List<StockDto> getStocksForExchange(Long exchangeId) {
        List<ExchangeToStockModel> models = repository.findAllByExchange_Id(exchangeId);
        return models.stream()
                .map(d -> stockConverter.toDto(d.getStock()))
                .collect(Collectors.toList());
    }

    public void deleteByStockId(Long stockId) {
        repository.deleteById(stockId);
    }

    public boolean save(ExchangeDto exchangeDto, List<StockDto> dtoList) {
        try {
            for (StockDto sd : dtoList) {
                repository.insert(exchangeDto.getId(), sd.getId());
            }
        } catch(ConversionException e) {
            return false;
        }

        return true;
    }
}
