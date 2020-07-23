package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StockExchangeRestController {
    private StockService stockService;
    private ExchangeService exchangeService;

    @Autowired
    public StockExchangeRestController(StockService stockService, ExchangeService exchangeService) {
        this.stockService = stockService;
        this.exchangeService = exchangeService;
    }

    @GetMapping("/stock/{id}")
    public List<StockModel> getStock(@PathVariable Long id) {
        return Collections.singletonList(stockService.getStock(id));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/exchange")
    public List<ExchangeDto> getAllExchanges() {
        return exchangeService.getAll();
    }

    @GetMapping("/exchange/{code}")
    public List<ExchangeDto> getExchange(@PathVariable String code) {
        return Collections.singletonList(exchangeService.get(code));
    }
}
