package com.github.owenliou.campkeeper.base.exception;

import com.github.owenliou.campkeeper.base.utils.text.StrUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

/**
 * 供 API 使用的自訂例外
 */
@Slf4j
public class RestCustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestCustomException(String errorMessage) {
        super(errorMessage);
    }

    public RestCustomException(String errorMessage, Object... arguments) {
        super(StrUtils.format(errorMessage, arguments));
    }

}
