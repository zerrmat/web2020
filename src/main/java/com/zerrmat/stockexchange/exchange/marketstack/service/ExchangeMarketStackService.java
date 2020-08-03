package com.zerrmat.stockexchange.exchange.marketstack.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepository;
import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepositoryFilter;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.exchange.service.ExchangeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExchangeMarketStackService {
    private ExchangeMarketStackRepository repository;
    private ExchangeConverter converter;

    @Autowired
    public ExchangeMarketStackService(ExchangeMarketStackRepository repository,
                                      ExchangeConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional
    public void updateExchanges(List<ExchangeDto> responseExchanges, List<ExchangeDto> dbExchanges) {
        List<ExchangeDto> obsoleteExchanges =
                ExchangeMarketStackRepositoryFilter.getObsoleteExchanges(responseExchanges, dbExchanges);
        obsoleteExchanges.forEach(e -> repository.deleteByCode(e.getCode()));

        List<ExchangeDto> newExchanges =
                ExchangeMarketStackRepositoryFilter.getNewExchanges(responseExchanges, dbExchanges);
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

    public List<ExchangeDto> getAll() {
        return converter.convertAllToDto(repository.findAll());
    }
}
