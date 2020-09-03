package com.study.login.global.security;

import com.study.login.domain.model.redis.Login;
import com.study.login.domain.repository.redis.LoginRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 토큰에 저장된 유저정보를 활용하여 유저를 인증하기 위해 {@link UserDetailsService} 추상체를
 * 내려 받아 재정의 한다.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final LoginRedisRepository loginRedisRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Login login = loginRedisRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new User(login.getUsername(), login.getPassword(), login.getAuthorities());
    }
}
