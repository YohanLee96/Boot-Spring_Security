package com.study.login.global.security;

import com.study.login.global.security.jwt.JwtAuthenticationFilter;
import com.study.login.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security를 사용하기 위해서는 Spring Security Filter Chain을 사용한다는것을 명시해줘야함.
 * WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 붙혀주면 된다.
 */

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정클래스 정의
@Configuration
public class SpringSecurityManager extends WebSecurityConfigurerAdapter {    //스프링 시큐리티를 설정하기위해 상속 받음.

    private final JwtTokenProvider provider;

    /**
     * 암호화에 필요한 PasswordEncoder 추상체를 Bean에 등록합니다.
     * @return 암호화 Encoding 추상체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager를 Bean에 등록합니다.
     * @return 등록한 AuthenticationManager
     * @throws Exception exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체
     * @param http HTTP 요청에 대한 웹 기반 보안을 구성할 수 있는 객체.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //Rest API만을 고려하여 기본설정 해지.
            .httpBasic().disable()
            // csrf 보안 토큰 Disable 처리.
            .csrf().disable()
            //토큰 기반 인증이므로 세션 역시 사용 안함.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            //[/admin/*] 형식의 URL은 ADMIN ROLE 부여.
            .antMatchers("/admin/**").hasRole("ADMIN")
                //[/user/*] 형식의 URL은 USER ROLE 부여.
            .antMatchers("/user/**").hasRole("USER")
                //이외 URL은 모든권한 부여.
            .anyRequest().permitAll()
            .and()
            //UsernamePasswordAuthenticationFilter 전에 JWT인증필터를 넣는다.
            .addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);


    }

}
