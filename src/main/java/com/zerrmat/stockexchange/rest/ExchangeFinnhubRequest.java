package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.finnhub.dto.ExchangeFinnhubResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeFinnhubRequest {
    Collection<ExchangeFinnhubResponse> elements;
}
