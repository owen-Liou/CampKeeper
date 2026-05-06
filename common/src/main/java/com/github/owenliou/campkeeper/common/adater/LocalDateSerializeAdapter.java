package com.github.owenliou.campkeeper.common.adater;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.owenliou.campkeeper.config.variables.DEFAULT_SETTINGS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * LocalDate 轉換至 yyyy-MM-dd
 */
public class LocalDateSerializeAdapter extends com.fasterxml.jackson.databind.JsonSerializer<LocalDate> implements JsonSerializer<LocalDate> {

    /**
     * gson
     * @param date the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     */
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(toDateString(date));
    }

    /**
     * Jackson
     * @param date Value to serialize; can <b>not</b> be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws IOException
     */
    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(toDateString(date));
    }

    private String toDateString(LocalDate date) {
        return date.format(DEFAULT_SETTINGS.DATE_FORMATTER_2);
    }

}
