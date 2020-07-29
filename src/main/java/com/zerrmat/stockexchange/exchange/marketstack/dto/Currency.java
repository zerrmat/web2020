package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    private String code;
    private String symbol;
    private String name;
}
