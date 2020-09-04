package com.study.login.domain.service;

import com.study.login.domain.dto.LoginDto;
import com.study.login.domain.model.User;
import com.study.login.domain.repository.redis.LoginRedisRepository;
import com.study.login.domain.repository.UserRepository;
import com.study.login.global.jwt.JwtTokenProvider;
import com.study.login.global.jwt.TokenSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final LoginRedisRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    /**
     * 유효한 유저인지 확인한 후, 로그인 토큰을 발급한다.
     * @param loginDto 로그인할 유저의 정보
     * @return 로그인과정을 정상적으로 마쳤을 경우, 토큰을 Set하여 리턴한다.
     */
    public LoginDto userLogin(LoginDto loginDto) {
        User user = userRepository.findByUserId(loginDto.getUserId())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 아이디입니다."));

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        TokenSet tokenSet = jwtTokenProvider.createTokenSet(user.getUserId(), Collections.singletonList(user.getRole().toString()));
        loginDto.registerTokenSet(tokenSet);

        //  Redis에 저장.
        loginRepository.save(loginDto.toEntity(user.getRole()));

        return loginDto;
    }
}
