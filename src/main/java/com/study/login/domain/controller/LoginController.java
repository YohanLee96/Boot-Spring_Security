package com.study.login.domain.controller;

import com.study.login.domain.dto.UserDto;
import com.study.login.domain.service.LoginService;
import com.study.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/join")
    @Transactional
    public ResponseEntity<UserDto> join(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(loginService.saveUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(loginService.userLogin(userDto));
    }


}
