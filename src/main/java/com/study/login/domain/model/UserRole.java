package com.study.login.domain.model;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("유저"),
    ADMIN("관리자");

    private final String koreanDesc;

    UserRole(String koreanDesc) {
        this.koreanDesc = koreanDesc;
    }
}
