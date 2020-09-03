package com.study.login.global.error;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ErrorMessage implements Serializable {

    final private String message;
    final private String code;

    @Builder
    ErrorMessage(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
