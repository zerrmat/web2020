package com.zerrmat.stockexchange.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.money.MonetaryAmount;

@Getter
@Setter
@Builder
public class StockDto {
    private Long id;
    private String name;
    private String symbol;
    private MonetaryAmount value;
}
