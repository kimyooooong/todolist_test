package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.common.CommonMap;
import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import com.todolist.form.LoginForm;
import com.todolist.form.TodoListForm;
import com.todolist.form.UpdateTodoListStatusForm;
import com.todolist.repository.TodoListRepository;
import com.todolist.service.TodoListService;
import com.todolist.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TodoListService todoListService;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private WebApplicationContext wac;


    @Autowired
    EntityManager entityManager;
    @Autowired
    ObjectMapper objectMapper;

    String jwtToken;

    Users users;

    @BeforeEach
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        users = Users.builder()
                .id("rladbdgns2")
                .password("rlapass2!!!")
                .nickName("kimkimkim2")
                .build();

        /////////////////////토큰 추출
        //회원가입.
        Users joinUser = usersService.join(users.getId(),users.getPassword(),users.getNickName());

        LoginForm loginForm = new LoginForm();
        loginForm.setId(users.getId());
        loginForm.setPassword(users.getPassword());

        //로그인 후 토큰 추출.
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        MvcResult result = resultActions.andReturn();
        CommonMap map = objectMapper.readValue(result.getResponse().getContentAsString(),CommonMap.class);
        LinkedHashMap<String,Object> maps = (LinkedHashMap) map.get("data");
        jwtToken = (String) maps.get("jwtToken");

        entityManager.clear();

    }

    @DisplayName("TODO LIST 컨트롤러 - TODO LIST 생성 ( 토큰 필요 )")
    @Test
    void testTodoListControllerAddTodoListTest() throws Exception {

        TodoListForm todoListForm = new TodoListForm();
        todoListForm.setTitle("TODOLIST_TITLE");
        todoListForm.setDesc("TODOLIST_DESC");

        mockMvc.perform(post("/todolists/add-todolist")
                        .content(objectMapper.writeValueAsString(todoListForm))
                        .header("Authentication" , jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        TodoList todoList = todoListRepository.findById(1L).get();
        assertThat("TODOLIST_TITLE", is(todoList.getTitle()));

        entityManager.clear();
    }


    @DisplayName("TODO LIST 컨트롤러 - 상태 업데이트  ( 토큰 필요 )")
    @Test
    void testTodoListControllerUpdateStatusTest() throws Exception {

        TodoList todoList = todoListService.addTodoList(users.getUserId(),"TITLE" , "DESC");

        assertThat(TodoListKind.TODO, is(todoList.getKind()));

        UpdateTodoListStatusForm form = new UpdateTodoListStatusForm();
        form.setTodoListKind(TodoListKind.ING);

        mockMvc.perform(put("/todolists/" + todoList.getTodoId() + "/status")
                        .content(objectMapper.writeValueAsString(form))
                        .header("Authentication" , jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        todoList = todoListRepository.findById(1L).get();
        assertThat(TodoListKind.ING, is(todoList.getKind()));

        entityManager.clear();
    }


    @DisplayName("TODO LIST 컨트롤러 - 최근 TODO LIST 조회  ( 토큰 필요 )")
    @Test
    void testTodoListControllerRecentTest() throws Exception {

        IntStream.range(0,10).forEach(c-> todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC"));

        UpdateTodoListStatusForm form = new UpdateTodoListStatusForm();
        form.setTodoListKind(TodoListKind.ING);

        ResultActions resultActions = mockMvc.perform(get("/todolists/recent")
                        .header("Authentication" , jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());


        MvcResult result = resultActions.andReturn();
        CommonMap map = objectMapper.readValue(result.getResponse().getContentAsString(),CommonMap.class);
        LinkedHashMap<String,Object> maps = (LinkedHashMap) map.get("data");
        Integer todoId = (Integer) maps.get("todoId");

        assertThat(todoId, is(10));

        entityManager.clear();

    }

    @DisplayName("TODO LIST 컨트롤러 - 전체 조회 (페이지 네이션 ) ( 최신순 정렬 ) ( 토큰 필요 ) ")
    @Test
    void testTodoListControllerAllTest() throws Exception {

        IntStream.range(0,50).forEach(c-> todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC"));

        ResultActions resultActions = mockMvc.perform(get("/todolists/all")
                        .header("Authentication" , jwtToken)
                        .param("page", "1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        CommonMap map = objectMapper.readValue(result.getResponse().getContentAsString(),CommonMap.class);
        List<LinkedHashMap<String,Object>> maps = (List<LinkedHashMap<String, Object>>) map.get("data");
        assertThat(maps.size(), is(20));

        entityManager.clear();

    }
}
