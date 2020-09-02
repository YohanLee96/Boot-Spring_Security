package com.study.login.service;

import com.study.login.model.redis.Login;
import com.study.login.repository.redis.LoginRedisRepository;
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
}
