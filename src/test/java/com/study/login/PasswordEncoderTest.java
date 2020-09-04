package com.study.login;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String PASSWORD = "xx02213xx";

    @Test
    @DisplayName("암호화 전,후 패스워드 matches()메소드를 비교할 수 있다.")
    void test() {


        String encodePassword = passwordEncoder.encode(PASSWORD);
        log.info("암호화된 패스워드 : {}", encodePassword);
        assertTrue(passwordEncoder.matches(PASSWORD, encodePassword));
    }

    @Test
    @DisplayName("암호화를 할때마다 값이 다르게 나온다.")
    void test2() {
        String password =  passwordEncoder.encode(PASSWORD);
        String password2 = passwordEncoder.encode(PASSWORD);
        log.info("암호화 : Before : {}, After : {}", PASSWORD, password);
        log.info("암호화 : Before : {}, After : {}", PASSWORD, password2);

        assertNotEquals(password, password2);
    }


}
