package com.zerrmat.stockexchange.ticker.marketstack.dto;

import com.zerrmat.stockexchange.marketstack.fragments.MarketStackTickerData;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerEODLatestMarketStackResponseWrapper {
    private MarketStackTickerData data;

    public TickerDto extract() {
        return TickerDto.builder()
                .open(data.getOpen())
                .high(data.getHigh())
                .low(data.getLow())
                .close(data.getClose())
                .volume(data.getVolume())
                .symbol(data.getSymbol())
                .date(data.getDate())
                .build();
    }
}
