package com.zerrmat.stockexchange.stock.dao;

import com.zerrmat.stockexchange.stock.dto.StockDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class StockRepositoryFilter {
    public List<StockDto> getObsoleteStocks(List<StockDto> responseStocks,
                                            List<StockDto> dbStocks) {
        Set<String> responseCodes = responseStocks.stream()
                .map(StockDto::getSymbol)
                .collect(Collectors.toSet());

        return dbStocks.stream()
                .filter(s -> !responseCodes.contains(s.getSymbol()))
                .collect(Collectors.toList());
    }

    public List<StockDto> getNewStocks(List<StockDto> responseStocks,
                                       List<StockDto> dbStocks) {
        Set<String> dbCodes = dbStocks.stream()
                .map(StockDto::getSymbol)
                .collect(Collectors.toSet());

        return responseStocks.stream()
                .filter(s -> !dbCodes.contains(s.getSymbol()))
                .collect(Collectors.toList());
    }
}
