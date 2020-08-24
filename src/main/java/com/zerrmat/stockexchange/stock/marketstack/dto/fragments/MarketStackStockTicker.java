package com.zerrmat.stockexchange.stock.marketstack.dto.fragments;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketStackStockTicker {
    private String name;
    private String symbol;
    private boolean has_intraday;
    private boolean has_eod;
}
