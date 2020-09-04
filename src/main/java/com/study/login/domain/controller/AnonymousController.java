package com.study.login.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anonymous")
public class AnonymousController {

    @GetMapping("/test")
    public String getRest() {
        return "This is Anonymous Service";
    }
}
