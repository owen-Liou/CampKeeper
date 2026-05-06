package com.github.owenliou.campkeeper.common.adater;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.github.owenliou.campkeeper.config.utils.DateUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * yyyy-MM-dd HH:mm:ss 轉換至 LocalDateTime
 */
public class LocalDateTimeToMillisecondDeserializeAdapter extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDateTime> implements JsonDeserializer<LocalDateTime> {

    /**
     * gson
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return toLocalDateTime(json.getAsLong());
    }

    /**
     * Jackson
     * @param p Parser used for reading JSON content
     * @param ctxt Context that can be used to access information about
     *   this deserialization activity.
     *
     * @throws IOException
     */
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return toLocalDateTime(p.getLongValue());
    }

    private LocalDateTime toLocalDateTime(long milli) {
//        return Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DateUtils.toLocalDateTime(milli);
    }

}