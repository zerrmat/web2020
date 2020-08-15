package com.zerrmat.stockexchange.stock.service;

import com.zerrmat.stockexchange.stock.dao.StockRepository;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private StockRepository repository;
    private StockConverter converter;

    @Autowired
    public StockService(StockRepository repository, StockConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public StockModel getStock(Long id) {
        return repository.findById(id).get();
    }

    public StockDto getBySymbol(String symbol) {
        StockModel model = repository.getBySymbol(symbol);
        return converter.toDto(model);
    }

    public List<StockDto> getAll() {
        List<StockModel> modelAll = repository.findAll();
        return converter.convertAllToDto(modelAll);
    }
}
