package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeMarketStackRequest {
    Collection<ExchangeMarketStackResponse> elements;
}
