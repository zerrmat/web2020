package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.service.StockService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExternalStocksController extends ExternalController {
    private String exchangeId;

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
        this.exchangeId = "";
    }

    @GetMapping("/external/exchanges/{exchangeId}/stocks")
    public List<StockDto> executeEndpoint(@PathVariable String exchangeId) {
        this.exchangeId = exchangeId;
        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        endpointName = "stocks." + exchangeId;
        return super.shouldUpdateData();
    }

    @Override
    protected List<StockDto> updateData() {
        List<StockDto> actualStockDtos = externalRequestsService
                .makeMarketStackStocksRequest(exchangeId, exchangeService);
        stockService.updateStocks(actualStockDtos, exchangeService.get(exchangeId));
        return actualStockDtos;
    }
}
