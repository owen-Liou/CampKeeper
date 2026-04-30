package com.github.owenliou.campkeeper.base.adater;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * yyyy-MM-dd 轉換至 LocalDate
 */
public class LocalDateDeserializeAdapter extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDate> implements JsonDeserializer<LocalDate> {

    /**
     * gson
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return toLocalDate(json.getAsString());
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
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return toLocalDate(p.getText());
    }

    private LocalDate toLocalDate(String dateString) {
        return LocalDate.parse(dateString, DEFAULT_SETTINGS.DATE_FORMATTER_2);
    }

}