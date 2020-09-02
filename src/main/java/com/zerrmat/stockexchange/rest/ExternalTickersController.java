package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExternalTickersController extends ExternalController {
    private String stockSymbol;

    private ExternalRequestsService externalRequestsService;

    public ExternalTickersController(CacheControlService cacheControlService,
                                     ExternalRequestsService externalRequestsService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
    }

    @GetMapping("/external/tickers/{stockSymbol}/eod/latest")
    public TickerDto executeEndpoint(@PathVariable String stockSymbol) {
        this.stockSymbol = stockSymbol;
        List<TickerDto> response = this.updateDataTemplateMethod();
        return response.get(0);
    }

    @Override
    protected boolean shouldUpdateData() {
        return true;
    }

    @Override
    protected List updateData() {
        List<TickerDto> tickerDtos = externalRequestsService.makeMarketStackTickersRequest(this.stockSymbol);
        return tickerDtos;
    }

    @Override
    protected void updateCache() {}
}
