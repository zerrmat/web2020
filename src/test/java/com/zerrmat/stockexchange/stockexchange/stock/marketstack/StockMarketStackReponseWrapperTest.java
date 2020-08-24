package com.zerrmat.stockexchange.stockexchange.stock.marketstack;

import com.zerrmat.stockexchange.exchange.marketstack.dto.fragments.MarketStackPagination;
import com.zerrmat.stockexchange.stock.dto.StockDto;
import com.zerrmat.stockexchange.stock.marketstack.dto.StockMarketStackResponseWrapper;
import com.zerrmat.stockexchange.stock.marketstack.dto.fragments.MarketStackStockData;
import com.zerrmat.stockexchange.stock.marketstack.dto.fragments.MarketStackStockTicker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StockMarketStackReponseWrapperTest {
    private StockMarketStackResponseWrapper wrapper;

    @Test
    public void shouldExtractData() {
        System.out.println(Monetary.getCurrencies());
        // given
        MarketStackPagination pagination = MarketStackPagination.builder()
                .limit(1000)
                .offset(0)
                .count(1000)
                .total(8948)
                .build();

        MarketStackStockTicker ticker1 = MarketStackStockTicker.builder()
                .name("CD Projekt")
                .symbol("CDR")
                .has_intraday(true)
                .has_eod(true)
                .build();
        MarketStackStockTicker ticker2 = MarketStackStockTicker.builder()
                .name("11 bit studios")
                .symbol("11B")
                .has_intraday(false)
                .has_eod(false)
                .build();
        List<MarketStackStockTicker> tickers = Arrays.asList(ticker1, ticker2);
        MarketStackStockData data = MarketStackStockData.builder()
                .name("Warsaw Stock Exchange")
                .acronym("GPW")
                .mic("XWAR")
                .country("Poland")
                .country_code("PL")
                .city("Warsaw")
                .website("www.gpw.pl")
                .tickers(tickers)
                .build();

        wrapper = StockMarketStackResponseWrapper.builder()
                .pagination(pagination)
                .data(data)
                .build();

        // when
        List<StockDto> result = wrapper.extract("PLN");

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result.get(0)).isNotNull();
        Assertions.assertThat(result.get(0).getName()).isEqualTo("CD Projekt");
        Assertions.assertThat(result.get(0).getSymbol()).isEqualTo("CDR");
        Assertions.assertThat(result.get(0).getValue()).isNotNull();
        Assertions.assertThat(result.get(0).getValue().getNumber()
                .numberValue(BigDecimal.class).compareTo(BigDecimal.valueOf(-1))).isEqualTo(0);
        Assertions.assertThat(result.get(0).getValue().getCurrency().getCurrencyCode()).isEqualTo("PLN");

        Assertions.assertThat(result.get(1)).isNotNull();
        Assertions.assertThat(result.get(1).getName()).isEqualTo("11 bit studios");
        Assertions.assertThat(result.get(1).getSymbol()).isEqualTo("11B");
        Assertions.assertThat(result.get(1).getValue()).isNotNull();
        Assertions.assertThat(result.get(1).getValue().getNumber()
                .numberValue(BigDecimal.class).compareTo(BigDecimal.valueOf(-1))).isEqualTo(0);
        Assertions.assertThat(result.get(1).getValue().getCurrency().getCurrencyCode()).isEqualTo("PLN");
    }
}
