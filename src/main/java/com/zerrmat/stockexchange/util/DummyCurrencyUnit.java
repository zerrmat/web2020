package com.zerrmat.stockexchange.util;

import javax.money.CurrencyContext;
import javax.money.CurrencyUnit;

public class DummyCurrencyUnit implements CurrencyUnit {
    @Override
    public String getCurrencyCode() {
        return "";
    }

    @Override
    public int getNumericCode() {
        return -1;
    }

    @Override
    public int getDefaultFractionDigits() {
        return 0;
    }

    @Override
    public CurrencyContext getContext() {
        return null;
    }

    @Override
    public int compareTo(CurrencyUnit o) {
        return 0;
    }
}
