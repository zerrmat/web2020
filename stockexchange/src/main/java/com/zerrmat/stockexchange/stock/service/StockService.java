package com.zerrmat.stockexchange.stock.service;

import com.zerrmat.stockexchange.stock.dao.StockRepository;
import com.zerrmat.stockexchange.stock.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockModel getStock(Long id) {
        StockModel one = stockRepository.findById(id).get();
        return one;
    }
}
