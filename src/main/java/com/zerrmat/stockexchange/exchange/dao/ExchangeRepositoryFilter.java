package com.zerrmat.stockexchange.exchange.dao;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class ExchangeRepositoryFilter {
    public List<ExchangeDto> getObsoleteExchanges(List<ExchangeDto> responseExchanges,
                                                   List<ExchangeDto> dbExchanges) {
        Set<String> responseCodes = responseExchanges.stream()
                .map(ExchangeDto::getSymbol)
                .collect(Collectors.toSet());

        return dbExchanges.stream()
                .filter(e -> !responseCodes.contains(e.getSymbol()))
                .collect(Collectors.toList());
    }

    public List<ExchangeDto> getNewExchanges(List<ExchangeDto> responseExchanges,
                                              List<ExchangeDto> dbExchanges) {
        Set<String> responseCodes = dbExchanges.stream()
                .map(ExchangeDto::getSymbol)
                .collect(Collectors.toSet());

        return responseExchanges.stream()
                .filter(e -> !responseCodes.contains(e.getSymbol()))
                .collect(Collectors.toList());
    }
}
