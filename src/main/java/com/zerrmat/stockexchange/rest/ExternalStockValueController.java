package com.zerrmat.stockexchange.rest;

import com.zerrmat.stockexchange.cachecontrol.dto.CacheControlDto;
import com.zerrmat.stockexchange.cachecontrol.service.CacheControlService;
import com.zerrmat.stockexchange.util.ExternalController;

import java.util.List;

public class ExternalStockValueController extends ExternalController {
    public ExternalStockValueController(CacheControlService cacheControlService) {
        super(cacheControlService);
    }

    @Override
    protected boolean shouldUpdateData() {
        String exchangeSymbol = "dummy";
        String stockSymbol = "stock";
        endpointName = "stocks." + exchangeSymbol + stockSymbol;

        CacheControlDto cacheControlDto = cacheControlService.getCacheDataFor(endpointName);
        if (cacheControlDto != null && !cacheControlDto.isStockCacheOutdated()) {
            return false;
        }
        return true;
    }

    /* TODO: real data for stocks from specific exchange, data has to be taken from:
        http://api.marketstack.com/v1/tickers/AAPL/intraday?access_key=166af8c956780fd148bc9dd925968daf&limit=1&symbols=AAPL&interval=15min (stock actual day, real-time data)
    */
    @Override
    protected List updateData() {
        return super.updateData();
    }
}
