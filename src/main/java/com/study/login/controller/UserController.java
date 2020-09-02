package com.study.login.controller;


import com.study.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRest(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
