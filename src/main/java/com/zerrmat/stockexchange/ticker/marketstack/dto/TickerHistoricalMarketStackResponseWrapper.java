package com.zerrmat.stockexchange.ticker.marketstack.dto;

import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.marketstack.fragments.MarketStackTickerData;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import lombok.*;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerHistoricalMarketStackResponseWrapper {
    private MarketStackPagination pagination;
    private List<MarketStackTickerData> data;

    public List<HistoricalDto> extract(String currency) {
        return data.stream().map(
                d -> HistoricalDto.builder()
                        .exchangeCurrency(currency)
                        .exchangeSymbol(d.getExchange())
                        .stockSymbol(d.getSymbol())
                        .value(Monetary.getDefaultAmountFactory().setCurrency(currency)
                                .setNumber(d.getClose()).create())
                        .volume(d.getVolume())
                        .date(d.getDate())
                        .build())
                .collect(Collectors.toList());
    }
}
