package com.study.login.global.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    private String accessToken;

    private String refreshToken;

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
