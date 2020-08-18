package com.zerrmat.stockexchange.exchange.service;

import com.zerrmat.stockexchange.exchange.dao.ExchangeRepository;
import com.zerrmat.stockexchange.exchange.dao.ExchangeRepositoryFilter;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExchangeService {
    private ExchangeRepository repository;
    private ExchangeConverter converter;
    private ExchangeRepositoryFilter repositoryFilter;

    @Autowired
    public ExchangeService(ExchangeRepository repository, ExchangeConverter converter,
                           ExchangeRepositoryFilter repositoryFilter) {
        this.repository = repository;
        this.converter = converter;
        this.repositoryFilter = repositoryFilter;
    }

    public ExchangeDto get(String code) {
        ExchangeModel model = repository.findBySymbol(code);
        return converter.toDto(model);
    }

    public List<ExchangeDto> getAll() {
        List<ExchangeModel> modelAll = repository.findAll();
        return converter.convertAllToDto(modelAll);
    }

    @Transactional
    public boolean updateExchanges(List<ExchangeDto> actualExchanges) {
        List<ExchangeDto> dbExchanges = this.getAll();
        List<ExchangeDto> obsoleteExchanges =
                repositoryFilter.getObsoleteExchanges(actualExchanges, dbExchanges);
        obsoleteExchanges.forEach(e -> repository.deleteBySymbol(e.getSymbol()));

        List<ExchangeDto> newExchanges =
                repositoryFilter.getNewExchanges(actualExchanges, dbExchanges);
        return this.save(newExchanges);
    }

    private boolean save(List<ExchangeDto> requestList) {
        try {
            List<ExchangeModel> modelList = converter.convertAllToEntity(requestList);
            for(ExchangeModel em : modelList) {
                repository.insert(em.getSymbol(), em.getCurrency(), em.getName());
            }
            repository.flush();
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }
}
