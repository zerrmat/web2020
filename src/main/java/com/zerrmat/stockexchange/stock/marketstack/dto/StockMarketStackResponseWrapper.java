package com.zerrmat.stockexchange.stock.marketstack.dto;

import com.zerrmat.stockexchange.marketstack.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.marketstack.fragments.stock.MarketStackStockData;
import lombok.*;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMarketStackResponseWrapper {
    private MarketStackPagination pagination;
    private MarketStackStockData data;

    public List<StockDto> extract(String exchangeCurrency) {
        return data.getTickers().stream().map(
                d -> StockDto.builder()
                        .name(d.getName())
                        .symbol(d.getSymbol())
                        .value(Monetary.getDefaultAmountFactory().setCurrency(exchangeCurrency)
                            .setNumber(BigDecimal.valueOf(-1)).create())
                        .build())
                .collect(Collectors.toList());
    }
}
