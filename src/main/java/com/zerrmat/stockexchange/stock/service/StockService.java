package com.zerrmat.stockexchange.stock.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
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
    private ExchangeToStockService etsService;

    @Autowired
    public StockService(StockRepository repository, StockConverter converter,
                        StockRepositoryFilter repositoryFilter, ExchangeToStockService etsService) {
        this.repository = repository;
        this.converter = converter;
        this.repositoryFilter = repositoryFilter;
        this.etsService = etsService;
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


    public boolean updateStocks(List<StockDto> actualStocks, ExchangeDto exchangeDto) {
        List<StockDto> dbStocks = etsService.getStocksForExchange(exchangeDto.getSymbol());
        List<StockDto> obsoleteStocks = repositoryFilter.getObsoleteStocks(actualStocks, dbStocks);

        obsoleteStocks.forEach(s -> {
            etsService.deleteByStockId(s.getId());
            repository.deleteBySymbol(s.getSymbol());
        });
        List<StockDto> newStocks = repositoryFilter.getNewStocks(actualStocks, dbStocks);

        return this.save(newStocks, exchangeDto);
    }

    private boolean save(List<StockDto> requestList, ExchangeDto exchangeDto) {
        try {
            List<StockModel> modelList = converter.convertAllToEntity(requestList);
            for(StockModel sm : modelList) {
                repository.insert(sm.getName(), sm.getValue(), sm.getCurrency(), sm.getSymbol());
            }
            repository.flush();
            for(StockModel sm : modelList) {
                StockModel stock = repository.getBySymbol(sm.getSymbol());
                //etsService.save();
            }
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
