package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.exchangetostock.dto.ExchangeToStockDto;
import com.zerrmat.stockexchange.exchangetostock.service.ExchangeToStockService;
import com.zerrmat.stockexchange.historical.dto.HistoricalDto;
import com.zerrmat.stockexchange.historical.service.HistoricalService;
import com.zerrmat.stockexchange.rest.service.ExternalRequestsService;
import com.zerrmat.stockexchange.util.ExternalController;
import org.springframework.stereotype.Controller;

import java.time.*;
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
    private LocalDate from;
    private LocalDate to;
    private List<ZonedDateTime> cachedDates;

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
        this.fullStockSymbol = stockSymbol;
        this.exchangeSymbol = exchangeSymbol;
        this.exchangeCurrency = exchangeCurrency;
        this.from = from;
        this.to = to;

        return this.updateDataTemplateMethod();
    }

    @Override
    protected boolean shouldUpdateData() {
        // should take data from db, if there's complete data, then omit update
        List<HistoricalDto> cachedData = historicalService.getHistoricalDataForStock(this.exchangeSymbol,
                this.fullStockSymbol);
        cachedData.sort(Comparator.comparing(HistoricalDto::getDate));
        cachedDates = cachedData.stream().map(HistoricalDto::getDate)
                .sorted(Comparator.comparing(ZonedDateTime::toLocalDate))
                .collect(Collectors.toList());
        if (cachedDates.size() == 0) {
            return true;
        } else {
            List<ZonedDateTime> possibleRequestDates = this.generateDates(from, to);
            possibleRequestDates = possibleRequestDates.stream()
                    .filter(d -> !containsDate(cachedDates, d))
                    .collect(Collectors.toList());

            if (possibleRequestDates.size() > 0) {
                from = possibleRequestDates.get(0).toLocalDate();
                to = possibleRequestDates.get(possibleRequestDates.size() - 1).toLocalDate();
                return true;
            } else {
                return false;
            }
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
        List<HistoricalDto> historicalDtos = externalRequestsService.makeMarketStackHistoricalRequest(
                this.exchangeCurrency, this.fullStockSymbol, this.from, this.to);
        ExchangeToStockDto one = exchangeToStockService.getOne(this.exchangeSymbol, this.fullStockSymbol);
        historicalDtos.forEach(h -> {
            h.setEtsId(one.getId());
            h.setExchangeId(one.getExchangeId());
            h.setExchangeName(one.getExchangeName());
            h.setStockId(one.getStockId());
            h.setStockName(one.getStockName());
        });

        historicalDtos = historicalDtos.stream()
                .filter(h -> !containsDate(cachedDates, h.getDate()))
                .collect(Collectors.toList());
        if (historicalDtos.size() != 0) {
            historicalService.insertData(historicalDtos);
        }

        return historicalDtos;
    }

    @Override
    protected void updateCache() {}

    private List<ZonedDateTime> generateDates(LocalDate from, LocalDate to) {
        List<ZonedDateTime> dates = new ArrayList<>();
        while (!from.isAfter(to) && from.isBefore(LocalDate.now())) {
            dates.add(ZonedDateTime.of(from, LocalTime.of(0,0), ZoneId.of("Etc/UTC")));
            from = from.plusDays(1);
        }
        dates = dates.stream()
                .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY)
                .filter(d -> d.getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());
        return dates;
    }

    @Override
    protected void resetState() {
        super.resetState();
        this.exchangeSymbol = null;
        this.exchangeCurrency = null;
        this.fullStockSymbol = null;
        this.from = null;
        this.to = null;
        this.cachedDates = null;
    }
}
