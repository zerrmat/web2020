package com.zerrmat.stockexchange.cachecontrol.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheControlDto {
    private String endpointName;
    private LocalDateTime lastAccess;

    public boolean isLongCacheOutdated() {
        return lastAccess.isBefore(LocalDateTime.now().minusDays(1));
    }

    public boolean isStockCacheOutdated() {
        return lastAccess.isBefore(LocalDateTime.now().minusMinutes(15));
    }
}
