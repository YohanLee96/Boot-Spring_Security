package com.study.login.controller;

import com.study.login.dto.UserDto;
import com.study.login.global.security.jwt.JwtTokenProvider;
import com.study.login.global.security.jwt.Token;
import com.study.login.service.LoginService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/join")
    @Transactional
    public ResponseEntity<UserDto> join(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(loginService.saveUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(loginService.userLogin(userDto));
    }

    @GetMapping("/accessDenied")
    public ResponseEntity<?> error() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication);
    }


}
