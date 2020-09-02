package com.study.login.model.redis;

import com.study.login.model.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis에 저장될 Data 구조
 */
@RedisHash("token")
@Getter @Setter
@NoArgsConstructor
public class Login implements UserDetails {
    @Id
    private String userId;

    private String accessToken;

    private String refreshToken;

    private List<String> roles = new ArrayList<>();

    @Builder
    public Login(String userId, String accessToken, String refreshToken, List<String> roles) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(this, null, this.getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return  "{noop}dummy";
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override   //사용자 계정이 만료되어 있는지 여부.
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override   //사용자 계정이 잠겨 있는지 여부.
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override   //사용자의 자격 증명(암호)가 만료되었는지 여부.
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override   //사용자의 사용가능 여부.
    public boolean isEnabled() {
        return true;
    }
}
