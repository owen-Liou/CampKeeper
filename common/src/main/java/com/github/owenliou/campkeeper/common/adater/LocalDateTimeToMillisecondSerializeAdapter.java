package com.github.owenliou.campkeeper.common.adater;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.owenliou.campkeeper.config.utils.DateUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * LocalDateTime 轉換至 ms
 */
public class LocalDateTimeToMillisecondSerializeAdapter extends com.fasterxml.jackson.databind.JsonSerializer<LocalDateTime> implements JsonSerializer<LocalDateTime> {

    /**
     * gson
     * @param date the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(toMilli(date));
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
    public void serialize(LocalDateTime date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(toMilli(date));
    }

    private long toMilli(LocalDateTime date) {
        return DateUtils.toMillis(date);
    }

}
