package com.study.login.domain.service;

import com.study.login.domain.dto.UserDto;
import com.study.login.domain.model.User;
import com.study.login.domain.repository.UserRepository;
import com.study.login.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public UserDto getUser(String accessToken) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserPk(accessToken))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        return user.toDto();
    }



}
