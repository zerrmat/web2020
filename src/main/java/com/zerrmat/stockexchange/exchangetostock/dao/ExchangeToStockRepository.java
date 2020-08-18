package com.zerrmat.stockexchange.exchangetostock.dao;

import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeToStockRepository extends JpaRepository<ExchangeToStockModel, Long> {
    List<ExchangeToStockModel> findAllByExchange_Symbol(String exchangeSymbol);
}
