package com.github.owenliou.campkeeper.common.exception.sso;

/**
 * SSO Token 過期例外
 */
public class SsoTokenExpiredException extends Exception {

    public SsoTokenExpiredException() {
        super("SSO access token or id token 已過期");
    }

    public SsoTokenExpiredException(String message) {
        super(message);
    }

    public SsoTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SsoTokenExpiredException(Throwable cause) {
        super(cause);
    }

    public SsoTokenExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
