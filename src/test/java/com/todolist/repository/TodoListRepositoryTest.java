package com.todolist.repository;

import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TodoListRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    private Users users;

    private TodoList todoList;

    @BeforeEach
    void setUp() {
        users = Users.builder()
                .id("rladbdgns")
                .password("rlapass")
                .nickName("kimkimkim")
                .build();

        usersRepository.save(users);

        todoList = TodoList.builder()
                .title("TODO LIST TITLE")
                .desc("TODO LIST DESC")
                .userId(users.getUserId())
                .kind(TodoListKind.TODO)
                .build();
    }

    @Test
    void testSaveTodoList() {
        TodoList resultTodoList = todoListRepository.save(todoList);
        assertThat(resultTodoList.getTodoId(), is(1L));
        assertThat(resultTodoList.getTitle(), is("TODO LIST TITLE"));
        assertThat(resultTodoList.getDesc(), is("TODO LIST DESC"));
        assertThat(resultTodoList.getUsersId(), is(1L));
    }

    @Test
    void testGetTodoList() {
        TodoList resultTodoList = todoListRepository.save(todoList);

        Optional<TodoList> result = todoListRepository.findById(resultTodoList.getTodoId());
        assertThat(result.get().getTitle(), is("TODO LIST TITLE"));
    }

    @Test
    void testUpdateTodoList() {
        TodoList resultTodoList = todoListRepository.save(todoList);
        resultTodoList.setKind(TodoListKind.ING);

        todoListRepository.save(todoList);
        Optional<TodoList> result = todoListRepository.findById(resultTodoList.getTodoId());

        assertThat(TodoListKind.ING, is(result.get().getKind()));
    }
}
