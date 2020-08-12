package com.zerrmat.stockexchange.util;

import org.springframework.core.convert.ConversionException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface GenericConverter<E,D> {
    D toDto(E entity) throws ConversionException;

    E toEntity(D data) throws ConversionException;

    default List<D> convertAllToDto(final Collection<E> entites){
        return entites.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    default List<E> convertAllToEntity(final Collection<D> data) {
        return data.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
}
