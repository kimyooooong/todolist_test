package com.todolist.controller;


import com.todolist.common.RestResponse;
import com.todolist.domain.Users;
import com.todolist.form.JoinForm;
import com.todolist.form.LoginForm;
import com.todolist.security.JwtTokenProvider;
import com.todolist.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.JoinColumn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UsersController {

    private final UsersService usersService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<RestResponse> join(
            @RequestBody JoinForm joinForm
            ) throws Exception {

        usersService.join(joinForm.getId() , joinForm.getPassword() , joinForm.getNickname());

        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(
            @RequestBody LoginForm loginForm
    ) throws Exception {


        Users user = usersService.login(loginForm.getId() , loginForm.getPassword());
        user.setJwtToken(jwtTokenProvider.createToken(loginForm.getId() , null));

        return ResponseEntity.ok(RestResponse.ok(user));
    }


    @PostMapping("/out/{id}")
    public ResponseEntity<RestResponse> out(
            @PathVariable Long id
    ) throws Exception {

        usersService.out(id);
        return ResponseEntity.ok(RestResponse.ok());
    }



}
