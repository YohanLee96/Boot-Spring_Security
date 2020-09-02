package com.study.login.model;

import com.study.login.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        this.password = "{noop}"+ password;
        this.role = role;
    }

    public UserDto toDto() {
        return UserDto.builder()
                .userId(this.userId)
                .password(this.password)
                .role(this.role)
                .build();
    }

}
