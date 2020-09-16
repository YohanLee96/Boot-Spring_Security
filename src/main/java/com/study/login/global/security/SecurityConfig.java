package com.study.login.global.security;

import com.study.login.domain.model.UserRole;
import com.study.login.domain.repository.UserRepository;
import com.study.login.domain.repository.redis.LoginRedisRepository;
import com.study.login.domain.service.FormUserDetailService;
import com.study.login.domain.service.RestUserDetailService;
import com.study.login.global.jwt.JwtFilter;
import com.study.login.global.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

/**
 * Spring Security를 사용하기 위해서는 Spring Security Filter Chain을 사용한다는것을 명시해줘야함.
 * WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 붙혀주면 된다.
 */


@Configuration
@EnableWebSecurity //Spring Security 설정클래스 정의
public class SecurityConfig {    //스프링 시큐리티를 설정하기위해 상속 받음.
    

    /**
     * Token 기반 REST 통신 Security 정책
     */
    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class tokenBaseConfig extends WebSecurityConfigurerAdapter {

        private final JwtTokenProvider provider;
        private final LoginRedisRepository loginRedisRepository;
        /**
         * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체
         * @param http HTTP 요청에 대한 웹 기반 보안을 구성할 수 있는 객체.
         */
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.
                    antMatcher("/api/**").
                    httpBasic().disable().  //Rest API만을 고려하여 기본설정 미사용.
                    formLogin().disable().  //Form Login 미사용.
                    csrf().disable().  // csrf 보안 토큰 미사용.
                    cors().
                    and().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS). //토큰 기반 인증이므로 세션 역시 사용 안함.
                    and().
                    authorizeRequests().
                    requestMatchers(CorsUtils::isPreFlightRequest).permitAll(). //CORS preflight 요청은 시큐리티 인증처리를 하지 않음.
                    //hasRole은 ROLE_ 접두사를 꼭 붙혀줘야 한다.
                    antMatchers("/user/**").hasAnyAuthority(UserRole.USER.toString(), UserRole.ADMIN.toString()).
                    antMatchers("/admin/**").hasAuthority(UserRole.ADMIN.toString()).
                    antMatchers("/**").permitAll(). //이외 다른 Mapping은 모두 허용.
                    and().
                    //UsernamePasswordAuthenticationFilter 전에 JWT인증필터를 넣는다.
                    addFilterBefore(new JwtFilter(provider), UsernamePasswordAuthenticationFilter.class);

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(new RestUserDetailService(loginRedisRepository));
        }

//        @Override
//        public UserDetailsService userDetailsService()  {
//            return new RestUserDetailService(loginRedisRepository);
//        }

    }

    /**
     * Form 기반 Security 정책
     */
    @Configuration
    @RequiredArgsConstructor
    public static class formBaseConfig extends WebSecurityConfigurerAdapter {

        private final UserRepository userRepository;

        private static final String LOGIN_URL = "/user/login";


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.
                    antMatcher("/**").
                    authorizeRequests().
                    antMatchers("/user/join").permitAll(). //신규회원가입페이지는 모두 접근가능.
                    antMatchers("/chat/**").permitAll(). //신규회원가입페이지는 모두 접근가능.
                    anyRequest().authenticated().
                    and().
                    formLogin().
                    loginPage(LOGIN_URL.intern()). //로그인 페이지는 모두 접근가능
                    defaultSuccessUrl("/"). //로그인 성공 시, 리다이렉트할 URL
                    successHandler(customSuccessHandler()).
                    failureUrl(String.format("%s?fail=true", LOGIN_URL)).
                    usernameParameter("userId").
                    passwordParameter("password").
                    permitAll().
                    and().
                    logout().
                    logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
                    logoutSuccessUrl(LOGIN_URL).
                    invalidateHttpSession(true);

        }

        /**
         * 로그인 성공 후 처리 Handler
         * @return AuthenticationSuccessHandler 구현체
         */
        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
            return new CustomLoginSuccessHandler();
        }

        @Override
        public void configure(WebSecurity web) {
            // 타임리프 View는 권한 없이 접근 가능하도록 ignoring() 설정.
            web.ignoring().antMatchers("/templates/**", "/webjars/**");

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(new FormUserDetailService(userRepository));
        }

    }

    /**
     * 암호화에 필요한 PasswordEncoder 추상체를 Bean에 등록합니다.
     * @return 암호화 Encoding 구현체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("johnxx1").password("1234").roles(UserRole.USER.name());
        auth.inMemoryAuthentication().withUser("johnxx2").password("1234").roles(UserRole.ADMIN.name());
        auth.inMemoryAuthentication().withUser("johnxx3").password("1234").roles(UserRole.USER.name());

    }


}
