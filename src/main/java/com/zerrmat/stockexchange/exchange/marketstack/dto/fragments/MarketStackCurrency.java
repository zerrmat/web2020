package com.zerrmat.stockexchange.exchange.marketstack.dto.fragments;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketStackCurrency {
    private String code;
    private String symbol;
    private String name;
}
