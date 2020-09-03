package com.study.login.domain.model;

import com.study.login.domain.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

/**
 * Spring Security는 UserDetails 구현체를 통해 권한 정보를 관리하기 때문에 {@link UserDetails}를 구현한다.
 */
@Table(name = "USER")
@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "seq")
    private long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password", length = 300, nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;



    @Builder
    public User(String userId, String password, UserRole role) {
        this.userId = userId;
        this.password = addNoOp(password);
        this.role = role;
    }

    public UserDto toDto() {
        return UserDto.builder()
                .userId(this.userId)
                .password(this.password)
                .role(this.role)
                .build();
    }

    private String addNoOp(String password) {
        return "{noop}" + password;
    }



}
