package com.study.login.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class FormAdminController {

    @GetMapping("/index")
    public String index() {
        return "/admin/index";
    }
}
