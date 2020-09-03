package com.study.login.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    JWT_EXPIRE_DATE("J001", "Token is Expired");



    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
