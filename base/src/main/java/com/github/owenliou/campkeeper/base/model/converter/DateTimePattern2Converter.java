package com.github.owenliou.campkeeper.base.model.converter;

import com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * yyyy-MM-dd HH:mm:ss
 */
@Converter
public class DateTimePattern2Converter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute == null)? null: DEFAULT_SETTINGS.DATE_TIME_FORMATTER_2.format(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? null : LocalDateTime.parse(dbData, DEFAULT_SETTINGS.DATE_TIME_FORMATTER_2);
    }

}
