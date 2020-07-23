package com.zerrmat.stockexchange.exchange.dao;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<ExchangeModel, Long> {
    ExchangeModel findByCode(String code);
}
