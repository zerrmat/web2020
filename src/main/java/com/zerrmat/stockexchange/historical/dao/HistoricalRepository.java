package com.zerrmat.stockexchange.historical.dao;

import com.zerrmat.stockexchange.historical.model.HistoricalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricalRepository extends JpaRepository<HistoricalModel, Long> {
}
