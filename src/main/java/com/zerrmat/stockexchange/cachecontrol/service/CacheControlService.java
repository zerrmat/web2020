package com.zerrmat.stockexchange.cachecontrol.service;

import com.zerrmat.stockexchange.cachecontrol.dao.CacheControlRepository;
import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.model.CacheControlModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheControlService {
    private CacheControlRepository repository;
    private CacheControlConverter converter;

    @Autowired
    public CacheControlService(CacheControlRepository repository, CacheControlConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public CacheControlDto getCacheDataFor(String endpointName) {
        CacheControlModel cacheControlModel = repository.getFirstByEndpointName(endpointName);
        return converter.toDto(cacheControlModel);
    }

    public boolean updateOne(CacheControlDto dto) {
        CacheControlModel cacheControlModel = repository.getFirstByEndpointName(dto.getEndpointName());
        cacheControlModel.setEndpointName(dto.getEndpointName());
        cacheControlModel.setLastAccess(dto.getLastAccess());

        try {
            repository.save(cacheControlModel);
            repository.flush();
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

}
