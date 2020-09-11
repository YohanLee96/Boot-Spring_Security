package com.study.login.domain.controller;

import com.study.login.domain.model.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/form")
public class FormLoginController {

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginpage() {
        return "/login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("roles", UserRole.values());
        return "/user/join";
    }
}
