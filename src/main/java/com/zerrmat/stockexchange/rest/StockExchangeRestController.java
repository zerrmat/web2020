package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StockExchangeRestController {
    private StockService stockService;

    @Autowired
    public StockExchangeRestController(StockService stockService) { this.stockService = stockService; }

    @GetMapping("/stock/{id}")
    public List<StockModel> getStock(@PathVariable Long id) {
        return Collections.singletonList(stockService.getStock(id));
    }
}
