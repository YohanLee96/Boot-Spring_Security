package com.study.login.global.security.jwt;

import com.study.login.model.redis.Login;
import com.study.login.service.RedisLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link JwtTokenProvider} JWT발행자를 이용하여 실제 유저 인증작업을 하는 Filter 클래스
 */

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisLoginService redisLoginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if(vaildTokenType(token)) {
            //token = token.replaceFirst("Bearer","");
            log.info("발행된 토큰 [{}]", token);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            //token이 존재하는지 여부와 토큰의 만료기간초과 여부를 확인한다.
            if (!jwtTokenProvider.validateToken(token)) {
                Login login = redisLoginService.getLoginInfo(authentication.getName());

                if(jwtTokenProvider.validateToken(login.getRefreshToken())) {
                    login.setAccessToken(jwtTokenProvider.createAccessToken(login.getUserId(), login.getRoles()));
                    authentication = login.toAuthentication();
                }

            }
            log.info("auth : [{}]",authentication.getAuthorities());
            //SecurityContext에 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //필터 등록
        filterChain.doFilter(request, response);
    }

    private boolean vaildTokenType(String token) {
        return token != null; //&& token.startsWith("Bearer");
    }

    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

}
