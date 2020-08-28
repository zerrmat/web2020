package com.zerrmat.stockexchange.cachecontrol.dao;

import com.zerrmat.stockexchange.cachecontrol.model.CacheControlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheControlRepository extends JpaRepository<CacheControlModel, Long> {
    CacheControlModel getFirstByEndpointName(String endpointName);
}
