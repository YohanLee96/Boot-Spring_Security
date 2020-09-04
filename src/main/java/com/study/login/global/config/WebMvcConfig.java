package com.study.login.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private final static String[] ENABLE_ORIGINS = {"*"};

    /**
     * WebMvc Flow에서 CORS 정책을 규정한다.
     * @param registry CORS 정책 메소드
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") //어떤 RequestMapping에 이 Custom CORS Setting을 적용할껀지..
                .allowedOrigins(ENABLE_ORIGINS) //허용할 Origin선택
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                        );
    }

}
