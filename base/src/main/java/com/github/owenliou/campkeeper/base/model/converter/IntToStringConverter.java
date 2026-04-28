package com.github.owenliou.campkeeper.base.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

@Converter
public class IntToStringConverter implements AttributeConverter<Integer, String> {

	@Override
	public String convertToDatabaseColumn(Integer attribute) {
		return String.valueOf(attribute);
	}

	@Override
	public Integer convertToEntityAttribute(String dbData) {
		return StringUtils.isBlank(dbData) ? 0 : Integer.parseInt(dbData);
	}

}
