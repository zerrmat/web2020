package com.zerrmat.stockexchange.stock.service;

import com.zerrmat.stockexchange.stock.dao.StockRepository;
import com.zerrmat.stockexchange.stock.dao.StockRepositoryFilter;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StockService {
    private StockRepository repository;
    private StockConverter converter;
    private StockRepositoryFilter repositoryFilter;

    @Autowired
    public StockService(StockRepository repository, StockConverter converter,
                        StockRepositoryFilter repositoryFilter) {
        this.repository = repository;
        this.converter = converter;
        this.repositoryFilter = repositoryFilter;
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

    @Transactional
    public boolean updateStocks(List<StockDto> actualStocks) {
        List<StockDto> dbStocks = this.getAll();
        List<StockDto> obsoleteStocks = repositoryFilter.getObsoleteStocks(actualStocks, dbStocks);
        //obsoleteStocks.forEach(s -> repository.deleteBySymbol(s.getSymbol()));

        List<StockDto> newStocks = repositoryFilter.getNewStocks(actualStocks, dbStocks);
        return this.save(newStocks);
    }

    private boolean save(List<StockDto> requestList) {
        try {
            List<StockModel> modelList = converter.convertAllToEntity(requestList);
            for(StockModel sm : modelList) {
                repository.insert(sm.getName(), sm.getValue(), sm.getCurrency(), sm.getSymbol());
            }
            repository.flush();
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
