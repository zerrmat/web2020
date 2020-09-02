package com.zerrmat.stockexchange.ticker.marketstack.dto;

import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerEODLatestMarketStackResponseWrapper {
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Long volume;
    private BigDecimal adj_open;
    private BigDecimal adj_high;
    private BigDecimal adj_low;
    private BigDecimal adj_close;
    private Long adj_volume;
    private String symbol;
    private String exchange;
    private ZonedDateTime date;

    public TickerDto extract() {
        return TickerDto.builder()
                .open(this.open)
                .high(this.high)
                .low(this.low)
                .close(this.close)
                .volume(this.volume)
                .symbol(this.symbol)
                .date(this.date)
                .build();
    }
}
