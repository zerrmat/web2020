package com.zerrmat.stockexchange.stock.dao;

import com.zerrmat.stockexchange.stock.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface StockRepository extends JpaRepository<StockModel, Long> {
    StockModel getBySymbol(String symbol);
    @Transactional
    void deleteBySymbol(String symbol);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO stock (name, value, currency, symbol)" +
            "VALUES (?1, ?2, ?3, ?4);", nativeQuery = true)
    void insert(String name, BigDecimal value, String currency, String symbol);
}
