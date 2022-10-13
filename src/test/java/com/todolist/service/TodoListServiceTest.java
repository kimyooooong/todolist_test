package com.todolist.service;
import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import com.todolist.repository.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TodoListServiceTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private TodoListService todoListService;

    @Autowired
    private TodoListRepository todoListRepository;


    private TodoList todoList;

    private Users users;

    @BeforeEach
    void setUp() throws Exception {

        users = Users.builder()
                .id("rladbdgns2")
                .password("rlapass2!!!")
                .nickName("kimkimkim2")
                .build();

        users  = usersService.join(users.getId(),users.getPassword(),users.getNickName());

    }


    @DisplayName("TODO 리스트 생성")
    @Test
    void testAddTodoList() {

        TodoList todoList = todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC");

        TodoList findTodoList = todoListRepository.findById(todoList.getTodoId()).get();

        assertThat(todoList.getTodoId(), is(findTodoList.getTodoId()));
    }

    @DisplayName("TODO 리스트 상태 변경")
    @Test
    void testUpdateStatusTodoList() {

        TodoList todoList = todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC");
        TodoList findTodoList = todoListRepository.findById(todoList.getTodoId()).get();
        assertThat(todoList.getTodoId(), is(findTodoList.getTodoId()));

        todoListService.updateStatusTodoList(findTodoList.getTodoId() , TodoListKind.ING);

        findTodoList = todoListRepository.findById(todoList.getTodoId()).get();

        assertThat(TodoListKind.ING, is(findTodoList.getKind()));
    }


    @DisplayName("TODO 리스트 - 최근 작성된 1개 조회")
    @Test
    void testRecentTodoList() {

        //10개 TODO 리스트 생성.
        IntStream.range(0,10).forEach(c-> todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC"));
        TodoList todoList = todoListService.getTodoListRecent();
        //10번째 인지 확인.
        assertThat(todoList.getTodoId() ,is(10L));


        //10개 더생성.
        IntStream.range(0,10).forEach(c-> todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC"));
        todoList = todoListService.getTodoListRecent();

        //20번째인지 확인.
        assertThat(todoList.getTodoId() ,is(20L));
    }


    @DisplayName("TODO 리스트 - 전체 조회 (페이지 네이션 ) ( 최신순 정렬 ) ")
    @Test
    void testTodoListAll() {

        int page = 1;
        int size = 20;

        //50개 TODO 리스트 생성.
        IntStream.range(0,50).forEach(c-> todoListService.addTodoList(users.getUserId() , "타이틀" , "DESC"));

        List<TodoList> todoListList = todoListService.getTodoListAll(PageRequest.of(page-1 , size , Sort.by("todoId").descending()));

        //최신순인지확인. 50개 중 가장 최신 생성된 아이디 50L
        assertThat(todoListList.get(0).getTodoId() ,is(50L));

        //20개 가져왔는지 확인.
        assertThat(todoListList.size() ,is(20));


        page = 3;
        //3페이지니 10개 가져왔는지 확인.
        todoListList = todoListService.getTodoListAll(PageRequest.of(page-1 , size , Sort.by("todoId").descending()));

        assertThat(todoListList.size() ,is(10));

    }

}
