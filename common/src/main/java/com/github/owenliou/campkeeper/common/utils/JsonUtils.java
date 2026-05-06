package com.github.owenliou.campkeeper.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.github.owenliou.campkeeper.common.adater.LocalDateDeserializeAdapter;
import com.github.owenliou.campkeeper.common.adater.LocalDateSerializeAdapter;
import com.github.owenliou.campkeeper.common.adater.LocalDateTimeToMillisecondDeserializeAdapter;
import com.github.owenliou.campkeeper.common.adater.LocalDateTimeToMillisecondSerializeAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonUtils {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeToMillisecondSerializeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeToMillisecondDeserializeAdapter())
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

}
