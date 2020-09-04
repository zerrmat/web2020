package com.zerrmat.stockexchange.historical.dto;

import lombok.*;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDto {
    private String exchangeName;
    private String exchangeSymbol;
    private String exchangeCurrency;
    private String stockName;
    private String stockSymbol;
    private MonetaryAmount value;
    private Long volume;
    private ZonedDateTime date;
}
