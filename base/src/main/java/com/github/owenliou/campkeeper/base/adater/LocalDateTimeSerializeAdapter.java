package com.github.owenliou.campkeeper.base.adater;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * LocalDateTime 轉換至 yyyy-MM-dd HH:mm:ss
 */
public class LocalDateTimeSerializeAdapter extends com.fasterxml.jackson.databind.JsonSerializer<LocalDateTime> implements JsonSerializer<LocalDateTime> {

    /**
     * gson
     */
    @Override
    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(toDateString(date));
    }

    /**
     * Jackson
     */
    @Override
    public void serialize(LocalDateTime date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(toDateString(date));
    }

    private String toDateString(LocalDateTime date) {
        return date.format(DEFAULT_SETTINGS.DATE_TIME_FORMATTER_2);
    }

}
