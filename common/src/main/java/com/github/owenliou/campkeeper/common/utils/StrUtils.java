package com.github.owenliou.campkeeper.common.utils;

import org.slf4j.helpers.MessageFormatter;

public class StrUtils {

    /**
     * format("Hi {}. My name is {}.", "Alice", "Bob");
     */
    public static String format(final String messagePattern, Object... arguments) {
        return MessageFormatter.arrayFormat(messagePattern, arguments).getMessage();
    }

}
