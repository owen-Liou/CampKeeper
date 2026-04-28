package com.github.owenliou.campkeeper.base.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Y/N(Database) <-> Boolean
 */
@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {        
        return (value != null && value) ? "Y" : "N";            
    }    

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equals(value);
    }

}
