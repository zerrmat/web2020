package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
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
    private ExchangeToStockService exchangeToStockService;
    private MarketStackController marketStackController;

    @Autowired
    public StockExchangeRestController(StockService stockService, ExchangeService exchangeService,
                                       ExchangeToStockService exchangeToStockService,
                                       MarketStackController marketStackController) {
        this.stockService = stockService;
        this.exchangeService = exchangeService;
        this.exchangeToStockService = exchangeToStockService;
        this.marketStackController = marketStackController;
    }

    @GetMapping("/stock/{id}")
    public List<StockModel> getStock(@PathVariable Long id) {
        return Collections.singletonList(stockService.getStock(id));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/exchange")
    public List<ExchangeDto> getAllExchanges() {
        marketStackController.updateExchanges();
        return exchangeService.getAll();
    }

    @GetMapping("/exchange/{code}")
    public List<ExchangeDto> getExchange(@PathVariable String code) {
        return Collections.singletonList(exchangeService.get(code));
    }

    @GetMapping("/exchange/{code}/stock")
    public List<StockDto> getAllStocks(@PathVariable String code) {
        return exchangeToStockService.getStocksForExchange(code);
    }
}
