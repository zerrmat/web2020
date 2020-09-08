package com.zerrmat.stockexchange.historical.dao;

import com.zerrmat.stockexchange.historical.model.HistoricalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Repository
public interface HistoricalRepository extends JpaRepository<HistoricalModel, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO historical (ets_id, value, volume, date)" +
            " VALUES (?1, ?2, ?3, ?4);", nativeQuery = true)
    void insert(Long etsId, BigDecimal value, Long volume, ZonedDateTime date);
}
