package com.zerrmat.stockexchange.marketstack.fragments;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketStackTickerData {
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
}
