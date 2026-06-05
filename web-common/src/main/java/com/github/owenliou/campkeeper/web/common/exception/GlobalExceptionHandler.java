package com.github.owenliou.campkeeper.web.common.exception;

import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.common.exception.CampNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 - 找不到資源
     */
    @ExceptionHandler(CampNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResult<Object> handleCampNotFoundException(CampNotFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
        return CustomResult.result(false, null);
    }

    /**
     * 400 - 請求參數驗證失敗（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResult<Object> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        return CustomResult.result(false, null);
    }

    /**
     * 500 - 未預期的例外，兜底處理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResult<Object> handleException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return CustomResult.result(false, null);
    }
}