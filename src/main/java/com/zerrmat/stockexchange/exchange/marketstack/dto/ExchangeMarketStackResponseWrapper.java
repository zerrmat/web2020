package com.zerrmat.stockexchange.exchange.marketstack.dto;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
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

    public List<ExchangeDto> extract() {
        return data.stream().map(
                d -> ExchangeDto.builder()
                        .name(d.getName())
                        .code(d.getMic())
                        .currency(d.getCurrency().getCode())
                        .build()).collect(Collectors.toList()
        );
    }
}
