package com.study.login.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * JWT토큰 발행자. 토큰을 직접적으로 발행하는 주체클래스이다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secretkey}")
    private String secretKey;

    //토큰 유효시간 지정.(30분)
    private final long tokenValidTime = 30 * 60 * 1000L;
    //리프레쉬 토큰 유효시간 지정(1일)
    private final long refreshTokenValidTime =  24 * 60 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        //Base64로 인코딩
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT 토큰을 생성합니다.
     * @param userPk 유저 유니크값
     * @param roles 유저의 ROLE
     * @return 생성한 JWT 토큰 값
     */
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); //JWT payload에 저장되는 정보 단위
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(now) //토큰 발행시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();
    }

    public String createRefreshToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); //JWT payload에 저장되는 정보 단위
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(now) //토큰 발행시간
                .setIssuer("refresh")
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) //Refresh토큰 만료시간은 하루.
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();
    }

    /**
     * JWT 토큰에서 인증정보 조회
     * @param token JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails =  userDetailsService.loadUserByUsername(this.getUserPk(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 회원정보 추출.
     * @param token JWT 토큰
     * @return 회원정보
     */
    private String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Request의 Header에서 token값을 가져옵니다. Header명 : X-AUTH-TOKEN
     * @param request HTTP 요청
     * @return token값
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     * @param jwtToken 검증할 JWT 토큰
     * @return
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }



}
