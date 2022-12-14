package com.todolist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.common.CommonMap;
import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import com.todolist.form.JoinForm;
import com.todolist.form.LoginForm;
import com.todolist.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersController usersController;

    @Autowired
    private UsersService usersService;

    @Autowired
    private WebApplicationContext wac;


    @Autowired
    EntityManager entityManager;
    @Autowired
    ObjectMapper objectMapper;

    Users users;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
                .alwaysDo(print())
                .build();

        users = Users.builder()
                .id("rladbdgns2")
                .password("rlapass2!!!")
                .nickName("kimkimkim2")
                .build();

    }

    @DisplayName("?????? ???????????? - ???????????? ?????????")
    @Test
    void testUsersControllerJoinTest() throws Exception {

        JoinForm joinForm = new JoinForm();
        joinForm.setId("rladbdgns");
        joinForm.setPassword("rladbdgns123!!");
        joinForm.setNickname("kimkimkim");

        ResultActions resultActions = mockMvc.perform(post("/users/join")
                        .content(objectMapper.writeValueAsString(joinForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        resultActions.andExpect(status().isOk());
    }

    @DisplayName("?????? ???????????? - ????????? ?????????")
    @Test
    void testUsersControllerLoginTest() throws Exception {


        //????????????.
        usersService.join(users.getId(),users.getPassword(),users.getNickName());

        LoginForm loginForm = new LoginForm();
        loginForm.setId(users.getId());
        loginForm.setPassword(users.getPassword());

        
        //?????????
        mockMvc.perform(post("/users/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }



    @DisplayName("?????? ???????????? - ???????????? ( ?????? ?????? ) ")
    @Test
    void testUsersControllerOutTest() throws Exception {

        //????????????.
        Users joinUser = usersService.join(users.getId(),users.getPassword(),users.getNickName());

        LoginForm loginForm = new LoginForm();
        loginForm.setId(users.getId());
        loginForm.setPassword(users.getPassword());

        //????????? ??? ?????? ??????.
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        MvcResult result = resultActions.andReturn();
        CommonMap map = objectMapper.readValue(result.getResponse().getContentAsString(),CommonMap.class);
        LinkedHashMap<String,Object> maps = (LinkedHashMap) map.get("data");
        String token = (String) maps.get("jwtToken");

        //????????? ?????? ?????????.
        entityManager.clear();

        //????????? ?????? ??????.
        mockMvc.perform(post("/users/out")
                .header("Authentication" , token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

        //?????? ??????????????? ??????.
        Throwable exception = assertThrows(RuntimeException.class, () -> usersService.getUsers(joinUser.getUserId()));
        assertThat("????????? ?????? ?????? ????????????.", is(exception.getMessage()));
    }

}
