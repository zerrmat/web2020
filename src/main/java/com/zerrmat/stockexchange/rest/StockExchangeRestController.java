package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.model.StockModel;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
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
    private HistoricalService historicalService;
    private ExchangeToStockService exchangeToStockService;
    private ExternalExchangesController externalExchangesController;
    private ExternalStocksController externalStocksController;
    private ExternalTickersController externalTickersController;
    private ExternalHistoricalController externalHistoricalController;

    @Autowired
    public StockExchangeRestController(StockService stockService, ExchangeService exchangeService,
                                       HistoricalService historicalService,
                                       ExchangeToStockService exchangeToStockService,
                                       ExternalExchangesController externalExchangesController,
                                       ExternalStocksController externalStocksController,
                                       ExternalTickersController externalTickersController,
                                       ExternalHistoricalController externalHistoricalController) {
        this.stockService = stockService;
        this.exchangeService = exchangeService;
        this.historicalService = historicalService;
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

    @GetMapping("exchange/{code}/stock/{id}/ticker/historical")
    public List<HistoricalDto> getHistoricalData(@PathVariable String code,
                                                 @PathVariable String id,
                                                 @RequestParam("from")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                 @RequestParam("to")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        String exchangeSymbol = code.toUpperCase();
        String stockSymbol = id.toUpperCase();

        ExchangeDto exchangeDto = exchangeService.getBySymbol(exchangeSymbol);
        List<StockDto> stocksForExchange = exchangeToStockService.getStocksForExchange(exchangeDto.getId());
        String finalStockSymbol = stockSymbol;
        long count = stocksForExchange.stream().filter(s -> s.getSymbol().equals(finalStockSymbol)).count();
        if (count == 1) {
            externalHistoricalController.executeEndpoint(exchangeDto.getSymbol(), exchangeDto.getCurrency(),
                    stockSymbol, from, to);
        } else {
            stockSymbol = stockSymbol + "." + exchangeDto.getSymbol();
            externalHistoricalController.executeEndpoint(null, exchangeDto.getCurrency(),
                    stockSymbol, from, to);
        }

        List<HistoricalDto> result = historicalService.getHistoricalDataForStock(exchangeSymbol, stockSymbol);

        return result;
    }
}
