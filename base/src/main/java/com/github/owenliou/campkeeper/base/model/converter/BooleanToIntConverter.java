package com.github.owenliou.campkeeper.base.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 0/1(Database) <-> boolean
 */
@Converter
public class BooleanToIntConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute)? 1: 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return Integer.valueOf(1).equals(dbData);
    }

}
