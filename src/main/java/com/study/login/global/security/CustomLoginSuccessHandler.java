package com.study.login.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        //인증 성공 이후, Referer값이 /login일 경우, 메인페이지로 이동.
        if(request.getHeader("referer").contains("/form/login")) {
            getRedirectStrategy().sendRedirect(request, response, request.getContextPath() + "/");
        }

    }
}
