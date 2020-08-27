package com.study.login.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * {@link JwtTokenProvider} JWT발행자를 이용하여 실제 유저 인증작업을 하는 Filter 클래스
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {    //Servlet 필터 상속

    private final JwtTokenProvider jwtTokenProvider; //JWT토큰 발행 클래스

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //HTTP Request Header에서 Token값을 가져옴.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        if(token != null) {
            //token이 존재하는지 여부와 토큰의 유효성 및 만료기간초과 여부를 확인한다.
            if (jwtTokenProvider.validateToken(token)) {
                //토큰이 유효하면 토큰으로부터 유저 정보를 받아옴.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                //SecurityContext에 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //만료가 됬다면 RefreshToken을 확인하고 새로운 토큰을 발급해준다.
                // TODO: 2020-08-27 Redis 에서 검증.

            }
        }

        //필터 등록
        filterChain.doFilter(servletRequest, servletResponse);  
    }
}
