package com.study.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.login.domain.dto.UserDto;
import com.study.login.domain.model.User;
import com.study.login.domain.model.UserRole;
import com.study.login.domain.model.redis.Login;
import com.study.login.domain.repository.UserRepository;
import com.study.login.domain.repository.redis.LoginRedisRepository;
import com.study.login.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ApplicationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRedisRepository loginRedisRepository;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private ObjectMapper objectMapper;


    private static final String ID = "dldygks12345";
    private static final String PASSWORD = "12345";
    private static final UserRole ROLE = UserRole.USER;


    @BeforeEach
    void init() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        this.objectMapper = new ObjectMapper();
    }

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
        userService.saveUser(userDto);

        User user = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(NullPointerException::new);

        //회원가입 시, 입력한 아이디와 회원가입된 아이디가 매칭되는지 확인.
        assertThat(ID).isEqualTo(user.getUserId());
    }


    @Test
    @DisplayName("신규가입한 사용자는 로그인을 할 수 있다.")
    @Transactional
    void loginTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .userId(ID)
                .password(PASSWORD)
                .role(ROLE)
                .build();

        //회원가입
        userService.saveUser(userDto);

        mvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인한 사용자정보는 Redis에 저장된다.")
    @Transactional
    void redisTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .userId(ID)
                .password(PASSWORD)
                .role(ROLE)
                .build();

        //회원가입
        userService.saveUser(userDto);

        //로그인
        mvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        Login login = loginRedisRepository.findById(ID).orElseThrow(NullPointerException::new);

        //로그인한 사용자ID가 Redis에 저장된 ID와 일치하는지 확인.
        assertThat(ID).isEqualTo(login.getUserId());
    }

    @Test
    @DisplayName("ADMIN 관련 서비스는 USER Role을 가진 사용자는 이용할 수 없다.")
    @WithMockUser(username = "yohan", authorities = {"USER"})
    void roleTest() throws Exception {
        mvc.perform(get("/admin/test")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("ADMIN 관련 서비스는 ADMIN Role을 가진 사용자만 이용할 수 있다.")
    @WithMockUser(username ="yohan", authorities = {"ADMIN"})
    void roleTest2() throws Exception {
        mvc.perform(get("/admin/test").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("아무 권한이 필요없는 서비스는 누구나 이용할 수 있다.")
    void roleTest3() throws Exception {
        mvc.perform(get("/anonymous/test").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
