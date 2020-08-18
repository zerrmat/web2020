package com.zerrmat.stockexchange.stock.dto;

import lombok.*;

import javax.money.MonetaryAmount;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Long id;
    private String name;
    private String symbol;
    private MonetaryAmount value;
}
