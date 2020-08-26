package com.zerrmat.stockexchange.exchangetostock.dao;

import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ExchangeToStockRepository extends JpaRepository<ExchangeToStockModel, Long> {
    List<ExchangeToStockModel> findAllByExchange_Id(Long exchangeId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO exchangetostock (exchange_id, stock_id) " +
            "VALUES (?1, ?2);", nativeQuery = true)
    void insert(Long exchangeId, Long stockId);
}
