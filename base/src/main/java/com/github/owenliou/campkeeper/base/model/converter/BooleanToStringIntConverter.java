package com.github.owenliou.campkeeper.base.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * '0'/'1'(Database) <-> boolean
 */
@Converter
public class BooleanToStringIntConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute)? "1": "0";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "1".equals(dbData);
    }

}
