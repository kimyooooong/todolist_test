package com.todolist.service;

import com.todolist.component.AES256;
import com.todolist.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AES256 aes256;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private Users users, users2 , users3;

    @BeforeEach
    void setUp() {
        users = Users.builder()
                .id("rlad")
                .password("rlapass")
                .nickName("kimkimkim")
                .build();

        users2 = Users.builder()
                .id("rladbdgns2")
                .password("rlapass")
                .nickName("kimkimkim2")
                .build();


        users3 = Users.builder()
                .id("rladbdgns2")
                .password("rlapass2!!!")
                .nickName("kimkimkim2")
                .build();
    }


    @DisplayName("회원가입 테스트")
    @Test
    void testJoinUsers() throws Exception {

        //아이디 검사 체크.
        Throwable exception = assertThrows(RuntimeException.class, () -> usersService.join(users.getId(),users.getPassword(),users.getNickName()));
        assertThat("아이디는 5~20자 사이의 영어 소문자 혹은 숫자만의 조합을 사용해주세요.", is(exception.getMessage()));

        //패스워드 검사 체크.
        exception = assertThrows(RuntimeException.class, () -> usersService.join(users2.getId(),users2.getPassword(),users2.getNickName()));
        assertThat("비밀번호는 8~20자 사이의 영어대소문자, 숫자, 특수문자 조합중 최소 2가지 조합을 사용해주세요.", is(exception.getMessage()));


        //전부다 통과 시 회원가입 여부 체크.
        Users result  = usersService.join(users3.getId(),users3.getPassword(),users3.getNickName());
        assertThat(result.getId(), is(aes256.encrypt(users3.getId())));

    }


    @DisplayName("로그인 테스트")
    @Test
    void testLoginUsers() throws Exception {

        //회원 가입.
        Users result  = usersService.join(users3.getId(),users3.getPassword(),users3.getNickName());

        //아이디 체크
        assertThat(result.getId(), is(aes256.encrypt(users3.getId())));

        //패스워드 체크
        assertThat(Boolean.TRUE, is(passwordEncoder.matches(users3.getPassword(),result.getPassword())));

        //패스워드잘못입력한경우.
        Throwable exception = assertThrows(RuntimeException.class, () -> usersService.login(users3.getId() , users.getPassword()));
        assertThat("아이디 혹은 패스워드가 다릅니다.", is(exception.getMessage()));
        
        //제대로 로그인한 경우
        Users loginUser = usersService.login(users3.getId() , users3.getPassword());
        assertThat(result.getUserId(),is(loginUser.getUserId()));

    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    void testOutUsers() throws Exception {

        //회원 가입.
        Users result  = usersService.join(users3.getId(),users3.getPassword(),users3.getNickName());

        assertThat(aes256.encrypt(users3.getId()), is(result.getId()));

        //회원 탈퇴.
        usersService.out(result.getUserId());

        //탈퇴 되었는지 확인.
        Throwable exception = assertThrows(RuntimeException.class, () -> usersService.getUsers(result.getUserId()));

        assertThat("유저가 존재 하지 않습니다.", is(exception.getMessage()));

    }






}
