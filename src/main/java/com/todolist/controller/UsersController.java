package com.todolist.controller;


import com.todolist.common.RestResponse;
import com.todolist.domain.Users;
import com.todolist.form.JoinForm;
import com.todolist.form.LoginForm;
import com.todolist.security.JwtTokenProvider;
import com.todolist.service.UsersService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.JoinColumn;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UsersController {

    private final UsersService usersService;

    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation("회원 가입 - 아이디 , 패스워드 , 닉네임 으로 회원 가입.")
    @PostMapping("/join")
    public ResponseEntity<RestResponse> join(
            @RequestBody JoinForm joinForm
            ) throws Exception {

        return ResponseEntity.ok(RestResponse.ok(usersService.join(joinForm.getId() , joinForm.getPassword() , joinForm.getNickname())));
    }

    @ApiOperation("로그인 - TODO LIST API 에 사용할 수 있는 토큰 발급.")
    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(
            @RequestBody LoginForm loginForm
    ) throws Exception {


        Users user = usersService.login(loginForm.getId() , loginForm.getPassword());
        List<String> role = List.of("ROLE_USER");
        user.setJwtToken(jwtTokenProvider.createToken(loginForm.getId() , role));

        return ResponseEntity.ok(RestResponse.ok(user));
    }

    @ApiOperation("토큰 인증 필요 - 회원 탈퇴.")
    @PostMapping("/out")
    public ResponseEntity<RestResponse> out(@RequestHeader("Authentication") String Header
    ) throws Exception {

        Users users = (Users) jwtTokenProvider.getAuthentication(Header).getPrincipal();

        usersService.out(users.getUserId());
        return ResponseEntity.ok(RestResponse.ok());
    }



}
