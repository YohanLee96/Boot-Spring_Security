package com.study.login;

import com.study.login.model.User;
import com.study.login.repository.UserRepository;
import com.study.login.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LoginTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;


    private static final String EMAIL = "kakao2@naver.com";



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

        //토큰에 저장된 name값과 email값이 일치하는지 확인.
        //Spring Security에서 USER_NAME값을 Email로 지정했기때문에 Email로 조회한다.
        assertThat(EMAIL).isEqualTo(authentication.getName());

    }

}
