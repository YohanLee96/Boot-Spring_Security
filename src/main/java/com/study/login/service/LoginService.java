package com.study.login.service;

import com.study.login.dto.UserDto;
import com.study.login.model.User;
import com.study.login.model.redis.Login;
import com.study.login.repository.redis.LoginRedisRepository;
import com.study.login.repository.UserRepository;
import com.study.login.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final LoginRedisRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 신규 회원을 저장한다.
     * @param userDto 저장할 유저정보
     * @return 정상적으로 저장이됬을 경우, 저장한 유저정보를 리턴한다.
     */
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        if(userRepository.existsByUserId(userDto.getUserId())) {
            throw new RuntimeException("이미 가입된 아이디입니다.");
        }

        User user = userDto.toEntity();
        userRepository.save(user);

        return  user.toDto();
    }

    /**
     * 유효한 유저인지 확인한 후, 로그인 토큰을 발급한다.
     * @param userDto 로그인할 유저의 정보
     * @return 로그인과정을 정상적으로 마쳤을 경우, 토큰을 Set하여 리턴한다.
     */
    public UserDto userLogin(UserDto userDto) {
        User user = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 아이디입니다."));

        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        userDto.setToken(jwtTokenProvider.createTokenSet(user.getUserId(), Collections.singletonList(user.getRole().toString())));
        userDto.setRole(user.getRole());
        //  Redis에 저장.
        loginRepository.save(Login.builder()
                .accessToken(userDto.getAccessToken())
                .refreshToken(userDto.getRefreshToken())
                .userId(userDto.getUserId())
                .roles(Collections.singletonList(userDto.getRole().toString()))
                .build());

        return userDto;
    }
}
