package com.study.login.global.security.jwt;

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

    private Date nowDate;

    //토큰 유효시간 지정.
    private static final long BASIC_TIME = 10 * 1000L;
    //리프레쉬 토큰 유효시간 지정
    private static final long REFRESH_TIME =  24 * 60 * 60 * 1000L;


    @PostConstruct
    protected void init() {
        //Base64로 인코딩
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.nowDate = new Date();
    }

    public String createAccessToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); //JWT payload에 저장되는 정보 단위
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(this.nowDate) //토큰 발행시간
                .setExpiration(new Date(this.nowDate.getTime() + BASIC_TIME)) //토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, this.secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();
    }

    public String createRefreshToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); //JWT payload에 저장되는 정보 단위
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(this.nowDate) //토큰 발행시간
                .setExpiration(new Date(this.nowDate.getTime() + REFRESH_TIME)) //Refresh토큰 만료시간은 하루.
                .signWith(SignatureAlgorithm.HS256, this.secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();
    }
}
