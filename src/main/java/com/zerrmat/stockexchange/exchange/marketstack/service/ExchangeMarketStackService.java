package com.zerrmat.stockexchange.exchange.marketstack.service;

import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepository;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.rest.ExchangeMarketStackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeMarketStackService {
    private ExchangeMarketStackRepository repository;
    private ExchangeMarketStackConverter converter;

    @Autowired
    public ExchangeMarketStackService(ExchangeMarketStackRepository repository, ExchangeMarketStackConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public boolean save(ExchangeMarketStackRequest requestList) {
        try {
            List<ExchangeModel> modelList = converter.convertAllToEntity(requestList.getElements());
            for(ExchangeModel em : modelList) {
                repository.save(em);
                repository.flush();
            }
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
