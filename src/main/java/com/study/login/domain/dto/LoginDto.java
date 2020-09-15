package com.study.login.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.login.domain.model.UserRole;
import com.study.login.domain.model.redis.Login;
import com.study.login.global.jwt.TokenSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;

@Getter
@Setter
public class LoginDto {
    private String userId;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String accessToken;

    private String refreshToken;



    public void registerTokenSet(TokenSet tokenSet) {
        this.accessToken = tokenSet.getAccessToken();
        this.refreshToken = tokenSet.getRefreshToken();
    }

    public Login toEntity(UserRole userRole) {
        return Login.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .userId(this.userId)
                .roles(Collections.singletonList(userRole.toString()))
                .build();
    }
}
