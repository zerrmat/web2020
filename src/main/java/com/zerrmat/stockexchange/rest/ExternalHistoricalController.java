package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ExternalHistoricalController extends ExternalController {
    private ExternalRequestsService externalRequestsService;
    private HistoricalService historicalService;
    private ExchangeToStockService exchangeToStockService;

    private String exchangeSymbol;
    private String exchangeCurrency;
    private String fullStockSymbol;
    private String stockSymbol;
    private LocalDate from;
    private LocalDate to;
    List<HistoricalDto> actualData;

    public ExternalHistoricalController(CacheControlService cacheControlService,
                                        ExternalRequestsService externalRequestsService,
                                        HistoricalService historicalService,
                                        ExchangeToStockService exchangeToStockService) {
        super(cacheControlService);
        this.externalRequestsService = externalRequestsService;
        this.historicalService = historicalService;
        this.exchangeToStockService = exchangeToStockService;
    }

    public List<HistoricalDto> executeEndpoint(String exchangeSymbol, String exchangeCurrency,
                                               String stockSymbol, LocalDate from, LocalDate to) {
        if (exchangeSymbol == null) {
            String[] split = stockSymbol.split("\\.");
            this.fullStockSymbol = split[0] + "." + split[1];
            this.exchangeSymbol = split[1];
        } else {
            this.fullStockSymbol = stockSymbol;
            this.exchangeSymbol = exchangeSymbol;
        }
        this.stockSymbol = stockSymbol;
        this.exchangeCurrency = exchangeCurrency;
        this.from = from;
        this.to = to;

        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        // should take data from db, if there's complete data, then omit update
        actualData = historicalService.getHistoricalDataForStock(this.exchangeSymbol,
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

    private boolean containsDate(List<ZonedDateTime> actualDates, ZonedDateTime date) {
        for(ZonedDateTime z : actualDates) {
            if (z.isEqual(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected List updateData() {
        List<ZonedDateTime> actualDates = actualData.stream().map(HistoricalDto::getDate)
                .sorted(Comparator.comparing(ZonedDateTime::toLocalDate))
                .collect(Collectors.toList());
        ZonedDateTime min = actualDates.get(0);
        ZonedDateTime max = actualDates.get(actualDates.size() - 1);

        List<HistoricalDto> historicalDtos = new ArrayList<>();
            historicalDtos = externalRequestsService.makeMarketStackHistoricalRequest(
                    this.exchangeCurrency, this.stockSymbol, this.from, this.to);
            ExchangeToStockDto one = exchangeToStockService.getOne(this.exchangeSymbol, this.fullStockSymbol);
            historicalDtos.stream().forEach(h -> {
                h.setEtsId(one.getId());
                h.setExchangeId(one.getExchangeId());
                h.setExchangeName(one.getExchangeName());
                h.setStockId(one.getStockId());
                h.setStockName(one.getStockName());
            });

            historicalDtos = historicalDtos.stream()
                    .filter(h -> !containsDate(actualDates, h.getDate()))
                    .collect(Collectors.toList());
            if (historicalDtos.size() != 0) {
                historicalService.insertData(historicalDtos);
            }

        return historicalDtos;
    }

    @Override
    protected void updateCache() {}
}
