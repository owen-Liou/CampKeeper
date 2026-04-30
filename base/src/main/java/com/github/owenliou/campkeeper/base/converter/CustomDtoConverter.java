package com.github.owenliou.campkeeper.base.converter;

import org.springframework.lang.NonNull;

import java.util.List;

public interface CustomDtoConverter<S, T> {

    /**
     * 1 比 1 轉換
     */
    List<T> convertEach(@NonNull List<S> source);

    /**
     * 轉換成單個
     */
    T convert(@NonNull List<S> source);

}
