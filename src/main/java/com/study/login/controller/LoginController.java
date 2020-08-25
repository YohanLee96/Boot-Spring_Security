package com.study.login.controller;

import com.study.login.dto.UserDto;
import com.study.login.model.User;
import com.study.login.repository.UserRepository;
import com.study.login.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;



    @PostMapping("/join")
    @Transactional
    public ResponseEntity<UserDto> join(@RequestBody UserDto userDto) {
        User user = userDto.toEntity();
        userRepository.save(user);
        user.setRoles(Collections.singletonList("ROLE_USER")); //최초 가입 시, USER로 설정.
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String jwtToken = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        log.info("생성된 JWT 토큰 : " + jwtToken);

        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> getRest(@RequestParam  String token) {
        return ResponseEntity.ok(jwtTokenProvider.getAuthentication(token).getName());
    }

}
