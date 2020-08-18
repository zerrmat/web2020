package com.zerrmat.stockexchange.exchangetostock.dto;

import lombok.*;

import javax.money.MonetaryAmount;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeToStockDto {
    private Long id;
    private Long exchangeId;
    private String exchangeName;
    private String exchangeSymbol;
    private Long stockId;
    private String stockName;
    private String stockSymbol;
    private MonetaryAmount stockValue;
}
