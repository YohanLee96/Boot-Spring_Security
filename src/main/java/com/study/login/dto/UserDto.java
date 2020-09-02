package com.study.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.login.model.User;
import com.study.login.global.security.jwt.Token;
import com.study.login.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {

    private String userId;

    private String password;

    private String accessToken;

    private String refreshToken;

    private UserRole role;

    public void setToken(Token token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }

    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .role(this.role)
                .build();
    }

    @Builder
    public UserDto(String userId, String password, String accessToken, String refreshToken, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
