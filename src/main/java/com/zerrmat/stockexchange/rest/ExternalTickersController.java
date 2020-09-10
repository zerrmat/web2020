package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class ExternalTickersController extends ExternalController {
    private String stockSymbol;
    private String exchangeSymbol;

    private ExternalRequestsService externalRequestsService;
    private StockService stockService;

    public ExternalTickersController(CacheControlService cacheControlService,
                                     ExternalRequestsService externalRequestsService,
                                     StockService stockService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.stockService = stockService;
    }

    @GetMapping("/external/tickers/{stockSymbol}/eod/latest")
    public List<StockDto> executeEndpoint(@PathVariable String stockSymbol) {
        this.stockSymbol = stockSymbol;

        List<StockDto> response = this.updateDataTemplateMethod();
        return response;
    }

    public List<StockDto> executeEndpoint(String stockSymbol, String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol;
        return this.executeEndpoint(stockSymbol);
    }

    // TODO: check cachecontrol name for each branch
    @Override
    protected boolean shouldUpdateData() {
        if (exchangeSymbol == null) {
            String[] split = stockSymbol.split("\\.");
            endpointName = "stocks." + split[1] + "." + split[0];
        } else {
            endpointName = "stocks." + exchangeSymbol + "." + stockSymbol;
        }
        return super.shouldUpdateData();
    }

    @Override
    protected List updateData() {
        List<TickerDto> tickerDtos = externalRequestsService.makeMarketStackTickersRequest(this.stockSymbol);
        List<StockDto> stockDtos = new ArrayList<>();
        if (tickerDtos.size() != 0) {
            stockDtos = Collections.singletonList(stockService.updateStockValue(tickerDtos.get(0)));
        }

        return stockDtos;
    }

    @Override
    protected void resetState() {
        super.resetState();
        this.stockSymbol = null;
        this.exchangeSymbol = null;
    }
}
