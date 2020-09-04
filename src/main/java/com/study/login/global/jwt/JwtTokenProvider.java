package com.study.login.global.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JWT토큰 발행자. 토큰을 직접적으로 발행하는 주체클래스이다.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtTokenBuilder jwtTokenBuilder;


    @Value("${jwt.secretkey}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT 토큰셋을 생성합니다.
     * @param userPk 유저 유니크값
     * @param roles 유저의 ROLE
     * @return Access TokenSet, Refresh TokenSet
     */
    public TokenSet createTokenSet(String userPk, List<String> roles) {
        return new TokenSet(jwtTokenBuilder.createAccessToken(userPk, roles), jwtTokenBuilder.createRefreshToken(userPk, roles));
    }

    /**
     * AccessToken을 생성합니다.
     * @param userPk 유저 유니크값
     * @param roles 유저의 ROLE
     * @return Access TokenSet
     */
    public String createAccessToken(String userPk, List<String> roles) {
        return jwtTokenBuilder.createAccessToken(userPk, roles);
    }

    /**
     * JWT 토큰에서  스프링 시큐리티 인증정보 조회
     * Spring SecurityContext에 저장할 때 쓰임.
     * @param token JWT 토큰
     * @return 스프링 시큐리티 인증 정보
     */
    public Authentication getAuthentication(String token) throws ExpiredJwtException {
        UserDetails userDetails =  userDetailsService.loadUserByUsername(this.getUserPk(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * 토큰에서 유저의 Subject값 추출.
     * @param token JWT 토큰
     * @return 회원 Subject값
     */
    public String getUserPk(String token) throws ExpiredJwtException {
        return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
    }

    /**
     * Request의 Header에서 token값을 가져옵니다. Header명 : Authorization
     * @param request HTTP 요청
     * @return token값
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /**
     * 토큰이 만료됬는지 확인
     * @param jwtToken 검증할 JWT 토큰
     * @return 검증 결과
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return claims.getBody().getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;

    }



}
