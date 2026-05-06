package com.github.owenliou.campkeeper.common.exception.sso;

import com.github.owenliou.campkeeper.common.utils.StrUtils;

public class ApiKeyInvalidException extends RuntimeException {

    public ApiKeyInvalidException(String message, Object... arguments) {
        super(StrUtils.format(message, arguments));
    }

}
