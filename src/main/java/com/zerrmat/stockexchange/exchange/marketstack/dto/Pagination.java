package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {
    private int limit;
    private int offset;
    private int count;
    private int total;
}
