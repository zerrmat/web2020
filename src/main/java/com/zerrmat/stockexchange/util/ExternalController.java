package com.zerrmat.stockexchange.util;

import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ExternalController {
    protected String endpointName;

    protected CacheControlService cacheControlService;

    @Autowired
    public ExternalController(CacheControlService cacheControlService){
        this.cacheControlService = cacheControlService;
    }

    protected final List updateDataTemplateMethod() {
        List result;
        if (!shouldUpdateData()) {
            return new ArrayList<>();
        }
        result = updateData();
        updateCache();

        return result;
    }

    protected boolean shouldUpdateData() {
        CacheControlDto cacheControlDto = cacheControlService.getCacheDataFor(endpointName);
        if (cacheControlDto != null && !cacheControlDto.isLongCacheOutdated()) {
            return false;
        }
        return true;
    }

    protected List updateData() {
        return new ArrayList<>();
    }

    protected void updateCache() {
        cacheControlService.updateOne(
                CacheControlDto.builder()
                        .endpointName(endpointName)
                        .lastAccess(LocalDateTime.now())
                        .build()
        );
    }
}
