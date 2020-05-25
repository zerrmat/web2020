package com.zerrmat.stockexchange.exchange.finnhub.dao;

import com.zerrmat.stockexchange.exchange.finnhub.model.ExchangeFinnhubModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ExchangeFinnhubRepository extends JpaRepository<ExchangeFinnhubModel, Long> {
}
