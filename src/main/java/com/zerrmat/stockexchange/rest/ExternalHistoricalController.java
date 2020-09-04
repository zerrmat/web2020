package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.ticker.dto.TickerDto;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ExternalHistoricalController extends ExternalController {
    private ExternalRequestsService externalRequestsService;
    private HistoricalService historicalService;

    private ExchangeDto exchangeDto;
    private String stockSymbol;
    private ZonedDateTime from;
    private ZonedDateTime to;

    public ExternalHistoricalController(CacheControlService cacheControlService,
                                        ExternalRequestsService externalRequestsService,
                                        HistoricalService historicalService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.historicalService = historicalService;
    }

    public List<HistoricalDto> executeEndpoint(ExchangeDto exchangeDto, String stockSymbol,
                                               ZonedDateTime from, ZonedDateTime to) {
        this.exchangeDto = exchangeDto;
        this.stockSymbol = stockSymbol;
        this.from = from;
        this.to = to;

        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        // should take data from db, if there's complete data, then omit update
        List<HistoricalDto> actualData = historicalService
                .getHistoricalDataForStock(this.exchangeSymbol, this.stockSymbol);
        actualData.sort(Comparator.comparing(HistoricalDto::getDate));
        ZonedDateTime fromDate = actualData.get(0).getDate();
        ZonedDateTime toDate = actualData.get(actualData.size() - 1).getDate();
        if (fromDate.isAfter(from) || toDate.isBefore(to)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected List updateData() {
        List<HistoricalDto> result = new ArrayList<>();
        externalRequestsService.makeMarketStackHistoricalRequest(this.stockSymbol, this.from, this.to);
        return ;
    }

    @Override
    protected void updateCache() {
        super.updateCache();
    }
}
