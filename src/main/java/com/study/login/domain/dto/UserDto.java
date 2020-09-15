package com.study.login.domain.dto;

import com.study.login.domain.model.User;
import com.study.login.domain.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class UserDto {

    @NotBlank(message = "userId 값은 필수 입니다.")
    private String userId;

    @NotBlank(message = "password 값은 필수 입니다.")
    private String password;

    @NotNull(message = "role 값은 필수 입니다.")
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
