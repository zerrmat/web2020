package com.zerrmat.stockexchange.marketstack.fragments.exchange;

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
