package com.study.login.domain.service;

import com.study.login.domain.dto.UserDto;
import com.study.login.domain.model.User;
import com.study.login.domain.repository.UserRepository;
import com.study.login.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 회원을 저장한다.
     * @param userDto 저장할 유저정보
     * @return 정상적으로 저장이됬을 경우, 저장한 유저정보를 리턴한다.
     */
    @Transactional
    public void saveUser(UserDto userDto) {
        if(userRepository.existsByUserId(userDto.getUserId())) {
            throw new RuntimeException("이미 가입된 아이디입니다.");
        }

        User user = userDto.toEntity();
        //패스워드 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

     //   return  user.toDto();
    }

    @Transactional(readOnly = true)
    public UserDto getUser(String accessToken) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserPk(accessToken))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        return user.toDto();
    }



}
