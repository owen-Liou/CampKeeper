package com.github.owenliou.campkeeper.base.exception;

public class ForbiddenException extends RestCustomException {

    public ForbiddenException(String errorMessage, Object... arguments) {
        super(errorMessage, arguments);
    }

}
