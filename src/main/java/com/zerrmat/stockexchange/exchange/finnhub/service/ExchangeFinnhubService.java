package com.zerrmat.stockexchange.exchange.finnhub.service;

import com.zerrmat.stockexchange.exchange.finnhub.dao.ExchangeFinnhubRepository;
import com.zerrmat.stockexchange.exchange.finnhub.model.ExchangeFinnhubModel;
import com.zerrmat.stockexchange.rest.ExchangeFinnhubRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeFinnhubService {
    private ExchangeFinnhubRepository repository;
    private ExchangeFinnhubConverter converter;

    @Autowired
    public ExchangeFinnhubService(ExchangeFinnhubRepository repository, ExchangeFinnhubConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public boolean save(ExchangeFinnhubRequest requestList) {
        try {
            List<ExchangeFinnhubModel> modelList = converter.convertAllToEntity(requestList.getElements());
            repository.saveAll(modelList);
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
