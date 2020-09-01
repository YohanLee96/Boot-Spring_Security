package com.study.login.service;

import com.study.login.dto.UserDto;
import com.study.login.model.User;
import com.study.login.model.redis.Login;
import com.study.login.repository.LoginRepository;
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
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 신규 회원을 저장한다.
     * @param userDto 저장할 유저정보
     * @return 정상적으로 저장이됬을 경우, 저장한 유저정보를 리턴한다.
     */
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        User user = userDto.toEntity();
        userRepository.save(user);
        user.setRoles(Collections.singletonList("ROLE_USER")); //최초 가입 시, USER로 설정.

        return  user.toDto();
    }

    /**
     * 유효한 유저인지 확인한 후, 로그인 토큰을 발급한다.
     * @param userDto 로그인할 유저의 정보
     * @return 로그인과정을 정상적으로 마쳤을 경우, 토큰을 Set하여 리턴한다.
     */
    public UserDto userLogin(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        userDto.setToken(jwtTokenProvider.createTokenSet(user.getUsername(), user.getRoles()));
        //  Redis에 저장.
        loginRepository.save(new Login(userDto.getAccessToken(), userDto.getRefreshToken()));

        return userDto;
    }
}
