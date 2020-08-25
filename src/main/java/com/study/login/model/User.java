package com.study.login.model;

import com.study.login.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security는 UserDetails 구현체를 통해 권한 정보를 관리하기 때문에 {@link UserDetails}를 구현한다.
 */
@Table(name = "USER")
@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //비 엔티티인 객체의 연관관계를 지정할때 사용하는 어노테이션
    private List<String> roles = new ArrayList<>();



    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Builder
    public User(String email, String password, List<String> roles) {
        this.email = email;
        this.password = "{noop}"+ password;
        this.roles = roles;
    }


    //**** Spring Security 관련 메소드 ****//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override   //내가 Spring Security의 UserName으로 사용할 정보는 이메일이니, 이메일 리턴.
    public String getUsername() {
        return this.email;
    }

    /**
     * 아래 재정의 메소드는 현재 딱히 비즈니스 로직이 없으니 모두 True처리.
     */

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
