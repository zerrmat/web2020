package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class ExternalStocksController extends ExternalController {
    private String exchangeSymbol;

    private ExternalRequestsService externalRequestsService;
    private ExchangeService exchangeService;
    private StockService stockService;

    public ExternalStocksController(CacheControlService cacheControlService,
                                    ExternalRequestsService externalRequestsService,
                                    ExchangeService exchangeService,
                                    StockService stockService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.exchangeService = exchangeService;
        this.stockService = stockService;
        this.exchangeSymbol = "";
    }

    @GetMapping("/external/exchanges/{exchangeSymbol}/stocks")
    public List<StockDto> executeEndpoint(@PathVariable String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol;
        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        endpointName = "stocks." + exchangeSymbol;
        return super.shouldUpdateData();
    }

    @Override
    protected List<StockDto> updateData() {
        List<StockDto> actualStockDtos = externalRequestsService
                .makeMarketStackStocksRequest(exchangeSymbol, exchangeService);
        stockService.updateStocks(actualStockDtos, exchangeService.getBySymbol(exchangeSymbol));
        return actualStockDtos;
    }

    @Override
    protected void resetState() {
        super.resetState();
        this.exchangeSymbol = null;
    }
}
