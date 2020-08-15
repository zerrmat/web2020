package com.zerrmat.stockexchange.stock.dao;

import com.zerrmat.stockexchange.stock.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockModel, Long> {
    StockModel getBySymbol(String symbol);
}
