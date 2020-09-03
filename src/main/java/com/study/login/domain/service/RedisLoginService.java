package com.study.login.domain.service;

import com.study.login.domain.model.redis.Login;
import com.study.login.domain.repository.redis.LoginRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLoginService {
    private final LoginRedisRepository loginRedisRepository;

    public Login getLoginInfo(String userName) {
        return loginRedisRepository.findById(userName)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 존재하지 않습니다."));
    }

    public void refreshToken(String userName, String accessToken) {
        Login login = this.getLoginInfo(userName);
        login.setAccessToken(accessToken);
        loginRedisRepository.save(login);
    }
}
