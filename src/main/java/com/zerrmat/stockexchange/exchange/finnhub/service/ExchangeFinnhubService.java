package com.zerrmat.stockexchange.exchange.finnhub.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.finnhub.dao.ExchangeFinnhubRepository;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.rest.ExchangeFinnhubRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionImpl;
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
            List<ExchangeModel> modelList = converter.convertAllToEntity(requestList.getElements());
            // TODO: Check if exchange exists in database
            for(ExchangeModel model: modelList) {
                repository.save(model);
                repository.flush();
            }
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
