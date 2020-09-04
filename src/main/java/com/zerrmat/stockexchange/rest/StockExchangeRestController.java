package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StockExchangeRestController {
    private StockService stockService;
    private ExchangeService exchangeService;
    private ExchangeToStockService exchangeToStockService;
    private ExternalExchangesController externalExchangesController;
    private ExternalStocksController externalStocksController;
    private ExternalTickersController externalTickersController;
    private ExternalHistoricalController externalHistoricalController;

    @Autowired
    public StockExchangeRestController(StockService stockService, ExchangeService exchangeService,
                                       ExchangeToStockService exchangeToStockService,
                                       ExternalExchangesController externalExchangesController,
                                       ExternalStocksController externalStocksController,
                                       ExternalTickersController externalTickersController,
                                       ExternalHistoricalController externalHistoricalController) {
        this.stockService = stockService;
        this.exchangeService = exchangeService;
        this.exchangeToStockService = exchangeToStockService;
        this.externalExchangesController = externalExchangesController;
        this.externalStocksController = externalStocksController;
        this.externalTickersController = externalTickersController;
        this.externalHistoricalController = externalHistoricalController;
    }

    @GetMapping("/stock/{id}")
    public List<StockModel> getStock(@PathVariable Long id) {
        return Collections.singletonList(stockService.getStock(id));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/exchange")
    public List<ExchangeDto> getAllExchanges() {
        externalExchangesController.executeEndpoint();
        return exchangeService.getAll();
    }

    @GetMapping("/exchange/{code}")
    public List<ExchangeDto> getExchange(@PathVariable String code) {
        return Collections.singletonList(exchangeService.getBySymbol(code));
    }

    @GetMapping("/exchange/{code}/stocks")
    public List<StockDto> getAllStocks(@PathVariable String code) {
        code = code.toUpperCase();
        externalStocksController.executeEndpoint(code);
        Long exchangeId = exchangeService.getBySymbol(code).getId();
        return exchangeToStockService.getStocksForExchange(exchangeId);
    }

    @GetMapping("exchange/{code}/stock/{id}/ticker/latest")
    public StockDto getLatestEODForStock(@PathVariable String code, @PathVariable String id) {
        code = code.toUpperCase();
        String stockId = id.toUpperCase();

        Long excId = exchangeService.getBySymbol(code).getId();
        List<StockDto> stocksForExchange = exchangeToStockService.getStocksForExchange(excId);
        long count = stocksForExchange.stream().filter(s -> s.getSymbol().equals(stockId)).count();
        if (count == 1) {
            externalTickersController.executeEndpoint(id, code);
            return stockService.getBySymbol(id);
        } else {
            String symbol = stockId + "." + code;
            externalTickersController.executeEndpoint(symbol);
            return stockService.getBySymbol(symbol);
        }
    }

    @GetMapping("exchange/{code}/stock{id}/ticker/historical")
    public List<TickerDto> getHistoricalData(@PathVariable String code,
                                             @PathVariable String id,
                                             @RequestParam("from") LocalDate from,
                                             @RequestParam("to") LocalDate to) {
        List<TickerDto> tickerDtos = new ArrayList<>();
        String exchangeSymbol = code.toUpperCase();
        String stockSymbol = id.toUpperCase();
        System.out.println(from);
        System.out.println(to);

        ExchangeDto exchangeDto = exchangeService.getBySymbol(exchangeSymbol);
        externalHistoricalController.executeEndpoint(exchangeDto, stockSymbol, from, to));

        return tickerDtos;
    }
}
