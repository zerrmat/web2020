package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeMarketStackResponse {
    private String name;
    private String mic;
    private String currency;
}