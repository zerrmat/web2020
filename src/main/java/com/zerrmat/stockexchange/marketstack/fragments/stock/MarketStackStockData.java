package com.zerrmat.stockexchange.marketstack.fragments.stock;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketStackStockData {
    private String name;
    private String acronym;
    private String mic;
    private String country;
    private String country_code;
    private String city;
    private String website;
    private List<MarketStackStockTicker> tickers;
}
