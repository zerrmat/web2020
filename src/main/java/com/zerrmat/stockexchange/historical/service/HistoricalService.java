package com.zerrmat.stockexchange.historical.service;

import com.zerrmat.stockexchange.historical.dao.HistoricalRepository;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.model.HistoricalModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricalService {
    private HistoricalRepository repository;
    private HistoricalConverter converter;

    public HistoricalService(HistoricalRepository repository, HistoricalConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public List<HistoricalDto> getHistoricalDataForStock(String exchangeSymbol, String stockSymbol) {
        List<HistoricalModel> all = repository.findAll();
        List<HistoricalModel> filtered;
        filtered = extractData(all, exchangeSymbol, stockSymbol);
        if (filtered.size() == 0) {
            String fixedStockSymbol = stockSymbol + "." + exchangeSymbol;
            filtered = extractData(all, exchangeSymbol, fixedStockSymbol);
        }
        return converter.convertAllToDto(filtered);
    }

    public List<HistoricalModel> extractData(List<HistoricalModel> data, String exchangeSymbol,
                                             String stockSymbol) {
        return data.stream()
                .filter(h -> h.getEts().getExchange().getSymbol().equals(exchangeSymbol))
                .filter(h -> h.getEts().getStock().getSymbol().equals(stockSymbol))
                .collect(Collectors.toList());
    }
}
