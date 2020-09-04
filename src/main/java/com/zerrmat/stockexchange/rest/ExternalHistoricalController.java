package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ExternalHistoricalController extends ExternalController {
    private ExternalRequestsService externalRequestsService;
    private HistoricalService historicalService;

    private ExchangeDto exchangeDto;
    private String stockSymbol;
    private LocalDate from;
    private LocalDate to;
    List<HistoricalDto> actualData;

    public ExternalHistoricalController(CacheControlService cacheControlService,
                                        ExternalRequestsService externalRequestsService,
                                        HistoricalService historicalService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.historicalService = historicalService;
    }

    public List<HistoricalDto> executeEndpoint(ExchangeDto exchangeDto, String stockSymbol,
                                               LocalDate from, LocalDate to) {
        this.exchangeDto = exchangeDto;
        this.stockSymbol = stockSymbol;
        this.from = from;
        this.to = to;

        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        // should take data from db, if there's complete data, then omit update
        actualData = historicalService.getHistoricalDataForStock(this.exchangeDto.getSymbol(),
                this.stockSymbol);
        actualData.sort(Comparator.comparing(HistoricalDto::getDate));
        if (actualData.size() > 0) {
            LocalDate fromDate = actualData.get(0).getDate().toLocalDate();
            LocalDate toDate = actualData.get(actualData.size() - 1).getDate().toLocalDate();

            if (fromDate.isAfter(from) || toDate.isBefore(to)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected List updateData() {
        List<HistoricalDto> historicalDtos = externalRequestsService.makeMarketStackHistoricalRequest(
                this.exchangeDto.getCurrency(), this.stockSymbol, this.from, this.to);
        historicalDtos = historicalDtos.stream()
                .filter(h -> !actualData.contains(h))
                .collect(Collectors.toList());
        if (historicalDtos.size() != 0) {
            historicalService.insertData(historicalDtos);
        }
        return historicalDtos;
    }

    @Override
    protected void updateCache() {}
}
