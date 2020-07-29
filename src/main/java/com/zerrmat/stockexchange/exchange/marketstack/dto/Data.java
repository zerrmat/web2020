package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Data {
    private String name;
    private String acronym;
    private String mic;
    private String country;
    private String country_code;
    private String city;
    private String website;
    private Timezone timezone;
    private Currency currency;
}
