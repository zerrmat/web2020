package com.zerrmat.stockexchange.exchange.marketstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timezone {
    private String timezone;
    private String abbr;
    private String abbr_dst;
}
