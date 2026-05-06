package com.github.owenliou.campkeeper.common.converter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

@Slf4j
public abstract class AbstractDtoConverter<S, T> extends org.modelmapper.AbstractConverter<S, T> implements Converter<S, T>, Function<S, T>, CustomDtoConverter<S, T> {

    protected ModelMapper modelMapper = new ModelMapper();

    protected Type sourceClass;

    protected Type targetClass;

    @PostConstruct
    private void postConstruct() {
        doPostConstruct();
    }

    protected void doPostConstruct() {
        // 修正 S = List<?> or T = List<?>
        this.sourceClass = TypeToken.of(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getType();
        this.targetClass = TypeToken.of(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getType();
    }

    @Override
    public @NonNull T convert(@NonNull S source) {
        if (targetClass == null) {
            doPostConstruct();
        }
        return doConvert(source);
    }

    public S reverse(@NonNull T target) {
        if (sourceClass == null) {
            doPostConstruct();
        }
        return doReverse(target);
    }

    @Override
    public T apply(S source) {
        return convert(source);
    }

    @Override
    public List<T> convertEach(List<S> source) {
        return source.stream().map(this::convert).toList();
    }

    @Override
    public T convert(@NonNull List<S> source) {
        return doConvert(source);
    }

    protected @NonNull T doConvert(final S source) {
        return modelMapper.map(source, targetClass);
    }

    protected @NonNull T doConvert(final List<S> source) {
        return modelMapper.map(source, targetClass);
    }

    protected S doReverse(T target) {
        return modelMapper.map(target, sourceClass);
    }

}
