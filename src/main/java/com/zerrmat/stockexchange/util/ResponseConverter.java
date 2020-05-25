package com.zerrmat.stockexchange.util;

import org.springframework.core.convert.ConversionException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface ResponseConverter<E, R> {
    E toEntity(R response) throws ConversionException;

    default List<E> convertAllToEntity(final Collection<R> responses) {
        return responses.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
}
