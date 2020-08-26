package com.study.login;

import com.study.login.model.User;
import com.study.login.repository.UserRepository;
import com.study.login.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class LoginTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL = "kakao2@naver.com";
    private static final long tokenValidTime = 10 * 1000L; //만료기한

    @Value("${jwt.secretkey}")
    private String secretKey;

    @BeforeEach
    void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    @Test
    @DisplayName("JWT 토큰 만료기한이 연장되는 지 확인한다.")
    void test3() {
        log.info("시크릿키 : [{}]", secretKey);
        Claims claims = Jwts.claims().setSubject(EMAIL); //JWT payload에 저장되는 정보 단위
        claims.put("roles", "ROLE_MEMBER");

        Date now = new Date();
        //토큰 발행
        String jwtToken =  Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(now) //토큰 발행시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();

          log.info("연장 전 : [{}]",Jwts.parser()
                  .setSigningKey(secretKey)
                  .parseClaimsJws(jwtToken)
                  .getBody()
                  .getExpiration());

        log.info("연장 후 : [{}]",Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getExpiration());
        
        
        
    }



    @Test
    @DisplayName("JWT토큰이 만료기한이 지날 경우, 인증이 안되는지 확인한다.")
    @Transactional
    void test2() throws InterruptedException {
        //회원가입
        User user = User.builder()
                .email(EMAIL)
                .password("12354")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
        Claims claims = Jwts.claims().setSubject(user.getUsername()); //JWT payload에 저장되는 정보 단위
        claims.put("roles", "ROLE_MEMBER");
        Date now = new Date();

        //토큰 발행
        String jwtToken =  Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(now) //토큰 발행시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 암호화 알고리즘과 시그니쳐에 들어갈 secret값 세팅.
                .compact();

        assertThat(this.validateToken(jwtToken)).isTrue();

        Thread.sleep(2000);

        assertThat(this.validateToken(jwtToken)).isFalse();
    }

    private boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }




    @Test
    @DisplayName("JWT토큰생성 후, 토큰에 있는 인증정보를 확인한다.")
    @Transactional
    void test() {

        //회원가입
        User user = User.builder()
                .email(EMAIL)
                .password("12354")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);

        // 회원가입한 이메일로 JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(EMAIL, Collections.singletonList("ROLE_USER"));

        //생성한 토큰에 들어있는 인증정보 확인.
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

        log.info("인증정보 [{}]", authentication);

        //토큰에 저장된 name값과 email값이 일치하는지 확인.
        //Spring Security에서 USER_NAME값을 Email로 지정했기때문에 Email로 조회한다.
        assertThat(EMAIL).isEqualTo(authentication.getName());

    }

}
