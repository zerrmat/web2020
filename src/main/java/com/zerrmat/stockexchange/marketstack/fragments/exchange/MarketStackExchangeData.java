package com.zerrmat.stockexchange.marketstack.fragments.exchange;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketStackExchangeData {
    private String name;
    private String acronym;
    private String mic;
    private String country;
    private String country_code;
    private String city;
    private String website;
    private MarketStackTimezone timezone;
    private MarketStackCurrency currency;
}
