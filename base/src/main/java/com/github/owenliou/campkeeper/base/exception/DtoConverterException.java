package com.github.owenliou.campkeeper.base.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DtoConverterException extends RuntimeException {

    public DtoConverterException(String errorMessage) {
        super(errorMessage);
    }

}
