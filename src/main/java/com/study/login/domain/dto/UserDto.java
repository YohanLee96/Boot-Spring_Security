package com.study.login.domain.dto;

import com.study.login.domain.model.User;
import com.study.login.domain.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    private String userId;

    private String password;

    private UserRole role;

    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .role(this.role)
                .build();
    }

    @Builder
    public UserDto(String userId, String password, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }
}
