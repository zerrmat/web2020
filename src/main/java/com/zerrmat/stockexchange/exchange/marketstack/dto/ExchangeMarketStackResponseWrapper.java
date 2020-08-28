package com.zerrmat.stockexchange.exchange.marketstack.dto;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.marketstack.fragments.exchange.MarketStackExchangeData;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
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
    private List<MarketStackExchangeData> data;

    public List<ExchangeDto> extract() {
        data.removeIf(d -> d.getCurrency() == null);
        return data.stream().map(
                d -> ExchangeDto.builder()
                        .name(d.getName())
                        .symbol(d.getMic())
                        .currency(d.getCurrency().getCode())
                        .build())
                .collect(Collectors.toList());
    }
}
