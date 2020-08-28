package com.zerrmat.stockexchange.marketstack.fragments.exchange;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketStackTimezone {
    private String timezone;
    private String abbr;
    private String abbr_dst;
}
