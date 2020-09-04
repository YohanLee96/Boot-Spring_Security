package com.study.login.global.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenSet {

    private String accessToken;

    private String refreshToken;

    public TokenSet(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
