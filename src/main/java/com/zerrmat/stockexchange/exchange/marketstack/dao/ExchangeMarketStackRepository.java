package com.zerrmat.stockexchange.exchange.marketstack.dao;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeMarketStackRepository extends JpaRepository<ExchangeModel, Long> {
}
