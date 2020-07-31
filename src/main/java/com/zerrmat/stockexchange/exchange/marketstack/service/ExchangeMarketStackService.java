package com.zerrmat.stockexchange.exchange.marketstack.service;

import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.marketstack.dao.ExchangeMarketStackRepository;
import com.zerrmat.stockexchange.exchange.marketstack.dto.ExchangeMarketStackResponse;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.rest.ExchangeMarketStackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeMarketStackService {
    private ExchangeMarketStackRepository repository;
    private ExchangeMarketStackConverter converter;

    @Autowired
    public ExchangeMarketStackService(ExchangeMarketStackRepository repository, ExchangeMarketStackConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    protected List<ExchangeDto> getObsoleteExchanges(List<ExchangeMarketStackResponse> responseExchanges,
                                                     List<ExchangeDto> dbExchanges) {
        Set<String> responseCodes = responseExchanges.stream()
                .map(ExchangeMarketStackResponse::getCode)
                .collect(Collectors.toSet());

        return dbExchanges.stream()
                .filter(e -> !responseCodes.contains(e.getCode()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateExchanges(List<ExchangeMarketStackResponse> responses, List<ExchangeDto> dbExchanges) {
        List<ExchangeDto> obsoleteExchanges = this.getObsoleteExchanges(responses, dbExchanges);

        obsoleteExchanges.forEach(e -> repository.deleteByCode(e.getCode()));
    }

    public boolean save(ExchangeMarketStackRequest requestList) {
        try {
            List<ExchangeModel> modelList = converter.convetAllToEntity(requestList.getElements());
            for(ExchangeModel em : modelList) {
                repository.insertOnConflictDoNothing(em.getCode(), em.getCurrency(), em.getName());
                repository.flush();
            }
        } catch (ConversionException e) {
            return false;
        }

        return true;
    }

    public List<ExchangeMarketStackResponse> getAll() {
        return converter.convertAllToDto(repository.findAll());
    }
}
