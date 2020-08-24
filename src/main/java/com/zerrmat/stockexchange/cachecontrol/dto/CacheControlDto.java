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

    public boolean isCacheOutdated() {
        return lastAccess.isBefore(LocalDateTime.now().minusDays(1));
    }
}