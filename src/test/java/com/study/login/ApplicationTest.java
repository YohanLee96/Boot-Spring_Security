package com.study.login;

import com.study.login.domain.dto.UserDto;
import com.study.login.domain.model.User;
import com.study.login.domain.model.UserRole;
import com.study.login.domain.repository.UserRepository;
import com.study.login.domain.service.LoginService;
import com.study.login.domain.service.UserService;
import com.study.login.global.security.jwt.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    //https://cheese10yun.github.io/spring-boot-test/


    private static final String ID = "dldygks1234";
    private static final String PASSWORD = "1234";
    private static final UserRole ROLE = UserRole.USER;

    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    void joinTest() {
        UserDto userDto = UserDto.builder()
                .userId(ID)
                .password(PASSWORD)
                .role(ROLE)
                .build();

        //회원가입 서비스
        loginService.saveUser(userDto);

        User user = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(NullPointerException::new);

        //회원가입 시, 입력한 아이디와 회원가입된 아이디가 매칭되는지 확인.
        assertThat(ID).isEqualTo(user.getUserId());
    }


    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {

        User user = userRepository.findAll().get(0);
        UserDto userDTO = UserDto.builder()
                .userId(user.getUserId())
                .password(user.getPassword().replace("{noop}",""))
                .build();

        //로그인 후, Token 획득.
        UserDto loginUserDTO = loginService.userLogin(userDTO);

        //획득한 Token으로 유저 인증.
        Authentication authentication = jwtTokenProvider.getAuthentication(loginUserDTO.getAccessToken());

        //Security Context에 등록된 UserName값과 UserId가 같은지 확인.
        assertThat(userDTO.getUserId()).isEqualTo(authentication.getName());
    }

    @Test
    @DisplayName("USER Role인 유저로  ADMIN 서비스에 요청")
    void roleTest() {
        User user = userRepository.findAllByRole(UserRole.USER).get(0);

        UserDto userDTO = UserDto.builder()
                .userId(user.getUserId())
                .password(user.getPassword().replace("{noop}",""))
                .build();

        //로그인
        UserDto loginUserDTO = loginService.userLogin(userDTO);
    }

    @Test
    @DisplayName("ADMIN Role인 유저로 ADMIN 서비스 요청")
    void roleTest2() {

    }
}
