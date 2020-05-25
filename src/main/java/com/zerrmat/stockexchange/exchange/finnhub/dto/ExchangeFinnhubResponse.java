package com.zerrmat.stockexchange.exchange.finnhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeFinnhubResponse {
    private String code;
    private String currency;
    private String name;
}
