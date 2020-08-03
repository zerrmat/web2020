package com.zerrmat.stockexchange.exchange.marketstack.dto;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackData;
import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackPagination;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeMarketStackResponseWrapper {
    private MarketStackPagination pagination;
    private List<MarketStackData> data;

    public List<ExchangeDto> extract() {
        return data.stream().map(
                d -> ExchangeDto.builder()
                        .name(d.getName())
                        .code(d.getMic())
                        .currency(d.getCurrency().getCode())
                        .build())
                .collect(Collectors.toList());
    }
}
