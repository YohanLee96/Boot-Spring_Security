package com.study.login.domain.rest;

import com.study.login.domain.dto.LoginDto;
import com.study.login.domain.dto.UserDto;
import com.study.login.domain.service.LoginService;
import com.study.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Validated UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.ok("success");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(loginService.userLogin(loginDto));
    }


}
