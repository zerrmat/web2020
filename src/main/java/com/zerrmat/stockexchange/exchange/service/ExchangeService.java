package com.zerrmat.stockexchange.exchange.service;

import com.zerrmat.stockexchange.exchange.dao.ExchangeRepository;
import com.zerrmat.stockexchange.exchange.dto.ExchangeDto;
import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
