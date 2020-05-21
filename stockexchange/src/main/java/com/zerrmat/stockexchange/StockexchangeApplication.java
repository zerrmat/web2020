package com.zerrmat.stockexchange;

import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StockexchangeApplication {
	private StockService stockService;

	@Autowired
	public StockexchangeApplication(StockService stockService) {
		this.stockService = stockService;
	}

	public static void main(String[] args) {
		SpringApplication.run(StockexchangeApplication.class, args);
	}

	@GetMapping("/stock/{id}")
	public StockModel getStock(@PathVariable Long id) {
		return stockService.getStock(id);
	}
}
