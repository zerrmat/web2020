package com.zerrmat.stockexchange.exchange.finnhub.dao;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeFinnhubRepository extends JpaRepository<ExchangeModel, Long> {
}
