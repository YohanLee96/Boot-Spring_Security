package com.study.login.domain.controller;


import com.study.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/my-info")
    public ResponseEntity<?> getRest(
            @RequestHeader(value="Authorization") String accessToken) {
        return ResponseEntity.ok(userService.getUser(accessToken));
    }
}
