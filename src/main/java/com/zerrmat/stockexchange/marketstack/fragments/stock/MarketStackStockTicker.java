package com.zerrmat.stockexchange.marketstack.fragments.stock;

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
