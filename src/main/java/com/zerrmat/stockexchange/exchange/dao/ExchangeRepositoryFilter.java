package com.zerrmat.stockexchange.exchange.dao;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExchangeRepositoryFilter {
    public static List<ExchangeDto> getObsoleteExchanges(List<ExchangeDto> responseExchanges,
                                                   List<ExchangeDto> dbExchanges) {
        Set<String> responseCodes = responseExchanges.stream()
                .map(ExchangeDto::getCode)
                .collect(Collectors.toSet());

        return dbExchanges.stream()
                .filter(e -> !responseCodes.contains(e.getCode()))
                .collect(Collectors.toList());
    }

    public static List<ExchangeDto> getNewExchanges(List<ExchangeDto> responseExchanges,
                                              List<ExchangeDto> dbExchanges) {
        Set<String> responseCodes = dbExchanges.stream()
                .map(ExchangeDto::getCode)
                .collect(Collectors.toSet());

        return responseExchanges.stream()
                .filter(e -> !responseCodes.contains(e.getCode()))
                .collect(Collectors.toList());
    }
}
