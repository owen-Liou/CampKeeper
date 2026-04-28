package com.github.owenliou.campkeeper.base.model.converter;

import com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * yyyy-MM-dd
 * {@link com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS.DATE_FORMATTER_2}
 * com.github.owenliou.campkeeper.variables
 */
@Converter
public class DatePattern2Converter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return (attribute == null)? null: DEFAULT_SETTINGS.DATE_FORMATTER_2.format(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? null : LocalDate.parse(dbData, DEFAULT_SETTINGS.DATE_FORMATTER_2);
    }

}
