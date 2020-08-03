package com.zerrmat.stockexchange.exchange.dao;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ExchangeRepository extends JpaRepository<ExchangeModel, Long> {
    ExchangeModel findByCode(String code);
    void deleteByCode(String code);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO exchange (code, currency, name)" +
            "VALUES (?1, ?2, ?3);", nativeQuery = true)
    void insert(String code, String currency, String name);
}
