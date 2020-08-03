package com.zerrmat.stockexchange.exchange.service;

import com.zerrmat.stockexchange.exchange.dao.ExchangeRepository;
import com.zerrmat.stockexchange.exchange.dao.ExchangeRepositoryFilter;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ExchangeService {
    private ExchangeRepository repository;
    private ExchangeConverter converter;

    @Autowired
    public ExchangeService(ExchangeRepository repository, ExchangeConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public ExchangeDto get(String code) {
        ExchangeModel model = repository.findByCode(code);
        return converter.toDto(model);
    }

    public List<ExchangeDto> getAll() {
        List<ExchangeModel> modelAll = repository.findAll();
        return converter.convertAllToDto(modelAll);
    }

    @Transactional
    public void updateExchanges(List<ExchangeDto> actualExchanges) {
        List<ExchangeDto> dbExchanges = this.getAll();
        List<ExchangeDto> obsoleteExchanges =
                ExchangeRepositoryFilter.getObsoleteExchanges(actualExchanges, dbExchanges);
        obsoleteExchanges.forEach(e -> repository.deleteByCode(e.getCode()));

        List<ExchangeDto> newExchanges =
                ExchangeRepositoryFilter.getNewExchanges(actualExchanges, dbExchanges);
        this.save(newExchanges);
    }

    private boolean save(List<ExchangeDto> requestList) {
        try {
            List<ExchangeModel> modelList = converter.convetAllToEntity(requestList);
            for(ExchangeModel em : modelList) {
                repository.insert(em.getCode(), em.getCurrency(), em.getName());
            }
            repository.flush();
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
