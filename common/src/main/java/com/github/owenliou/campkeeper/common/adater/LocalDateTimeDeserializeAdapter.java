package com.github.owenliou.campkeeper.common.adater;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.github.owenliou.campkeeper.config.variables.DEFAULT_SETTINGS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * yyyy-MM-dd HH:mm:ss 轉換至 LocalDateTime
 */
public class LocalDateTimeDeserializeAdapter extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDateTime> implements JsonDeserializer<LocalDateTime> {

    /**
     * gson
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return toDateTime(json.getAsString());
    }

    /**
     * Jackson
     */
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return toDateTime(p.getValueAsString());
    }

    private LocalDateTime toDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DEFAULT_SETTINGS.DATE_TIME_FORMATTER_2);
    }

}
