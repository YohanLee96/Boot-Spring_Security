package com.study.login.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenBuilder {

    @Value("${jwt.secretkey}")
    private String secretKey;

    //토큰 유효시간 지정.
    private static final long BASIC_TIME = 30 * 60 * 1000L;
    //리프레쉬 토큰 유효시간 지정
    private static final long REFRESH_TIME =  24 * 60 * 60 * 1000L;


    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); //Base64로 인코딩
    }

    public String createAccessToken(String userName, List<String> roles) {
        return createToken(userName, roles, makeExpireDate(BASIC_TIME));
    }

    public String createRefreshToken(String userName, List<String> roles) {
        return createToken(userName, roles, makeExpireDate(REFRESH_TIME));
    }

    private String createToken(String userName, List<String> roles, Date expireDate) {
        Claims claims = Jwts.claims().setSubject(userName); //JWT payload에 저장되는 정보 단위
        claims.put("role", roles);

        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(new Date()) //토큰 발행시간
                .setExpiration(expireDate) //토큰 만료시간
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS256, this.secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();
    }

    private Date makeExpireDate(long addTime) {
        return new Date(new Date().getTime()+ addTime);
    }
}
