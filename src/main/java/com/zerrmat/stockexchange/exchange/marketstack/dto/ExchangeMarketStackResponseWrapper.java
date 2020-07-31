package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeMarketStackResponseWrapper {
    private Pagination pagination;
    private List<Data> data;

    public List<ExchangeMarketStackResponse> extractResponse() {
        return data.stream().map(
                d -> ExchangeMarketStackResponse.builder()
                        .name(d.getName())
                        .code(d.getMic())
                        .currency(d.getCurrency().getCode())
                        .build()).collect(Collectors.toList()
        );
    }
}
