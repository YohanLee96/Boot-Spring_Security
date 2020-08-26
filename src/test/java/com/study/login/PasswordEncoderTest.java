package com.study.login;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("PasswordEncoder.matches메소드 테스트")
    void test() {
        String password = "12345";
        String password2 = "122342342354235345";
        String password_end = passwordEncoder.encode(password);

        log.info("인코딩 전 : {}", password);
        log.info("인코딩 후 : {}", password_end);
        //인코딩한 비번과 안한 비번 매칭 테스트
        boolean isMatch = passwordEncoder.matches(password, password_end);
        Assertions.assertThat(isMatch).isTrue();
    }
}
