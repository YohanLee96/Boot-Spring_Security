package com.study.login.model.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Redis에 저장될 Data 구조
 */
@RedisHash("token")
@AllArgsConstructor
@Getter @Setter
public class Login {
    
    @Id
    private String accessToken;
    
    private String refreshToken;

}
