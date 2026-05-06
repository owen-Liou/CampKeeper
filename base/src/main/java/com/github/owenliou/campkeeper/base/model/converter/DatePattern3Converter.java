package com.github.owenliou.campkeeper.base.model.converter;

import com.github.owenliou.campkeeper.config.variables.DEFAULT_SETTINGS;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * yyyyMMdd
 */
@Converter
public class DatePattern3Converter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return (attribute == null)? null: DEFAULT_SETTINGS.DATE_FORMATTER_3.format(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? null : LocalDate.parse(dbData, DEFAULT_SETTINGS.DATE_FORMATTER_3);
    }

}
