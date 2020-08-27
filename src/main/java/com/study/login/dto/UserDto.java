package com.study.login.dto;

import com.study.login.model.User;
import com.study.login.global.security.jwt.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private String email;

    private String password;

    private String accessToken;

    private String refreshToken;

    private List<String> roles;

    public void setToken(Token token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }

    @Builder
    public UserDto(String email, String password, String accessToken, String refreshToken, List<String> roles) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }
}
