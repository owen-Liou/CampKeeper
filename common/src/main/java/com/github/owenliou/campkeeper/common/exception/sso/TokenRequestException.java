package com.github.owenliou.campkeeper.common.exception.sso;

import com.github.owenliou.campkeeper.common.utils.StrUtils;

/**
 * Token 例外
 */
public class TokenRequestException extends RuntimeException {

    public TokenRequestException(String message) {
        super(message);
    }

    public TokenRequestException(String message, Throwable cause, Object... arguments) {
        super(StrUtils.format(message, arguments), cause);
    }
}
