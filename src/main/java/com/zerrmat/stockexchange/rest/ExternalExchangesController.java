package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.service.ExchangeService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExternalExchangesController extends ExternalController {
    private ExternalRequestsService externalRequestsService;
    private ExchangeService exchangeService;

    public ExternalExchangesController(CacheControlService cacheControlService,
                                       ExternalRequestsService externalRequestsService,
                                       ExchangeService exchangeService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.exchangeService = exchangeService;
    }

    @GetMapping("/external/exchanges")
    public List<ExchangeDto> executeEndpoint() {
        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        endpointName = "exchanges";
        return super.shouldUpdateData();
    }

    @Override
    protected List updateData() {
        return externalRequestsService
                .makeMarketStackExchangesRequest(exchangeService);
    }
}
