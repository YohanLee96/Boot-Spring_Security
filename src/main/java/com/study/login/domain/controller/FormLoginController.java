package com.study.login.domain.controller;

import com.study.login.domain.dto.UserDto;
import com.study.login.domain.model.UserRole;
import com.study.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class FormLoginController {

    private final UserService userService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginpage() {
        return "/user/login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("roles", UserRole.values());
        return "/user/join";
    }

    @PostMapping("/join")
    public String execJoin(UserDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/user/login";
    }

    @GetMapping("/my-info")
    public String myInfo() {
        return "/user/myInfo";
    }
}
