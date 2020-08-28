package com.zerrmat.stockexchange.marketstack.fragments;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketStackPagination {
    private int limit;
    private int offset;
    private int count;
    private int total;
}
