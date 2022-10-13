package com.todolist.service;


import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import com.todolist.exception.ServiceException;
import com.todolist.repository.TodoListRepository;
import com.todolist.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    private final UsersService usersService;

    public TodoList getTodoListRecent(){
        return todoListRepository.findOneOrderByTodoIdOrderDesc().orElseThrow(()->  new ServiceException("TODO 리스트가 존재 하지 않습니다."));

    }

    public List<TodoList> getTodoListAll(Pageable pageable){
        return todoListRepository.findAll(pageable).getContent();
    }

    @Transactional
    public TodoList addTodoList(Long userId, String title , String desc) throws Exception {

        return todoListRepository.save(TodoList.builder()
                .title(title)
                .desc(desc)
                .userId(userId)
                .kind(TodoListKind.TODO)
                .build());
    }

    @Transactional
    public void updateStatusTodoList(Long todoListId , TodoListKind todoListKind){


        TodoList todoList = todoListRepository.findById(todoListId).orElseThrow(()->  new ServiceException("TODO 리스트가 존재 하지 않습니다."));
        todoList.setKind(todoListKind);

        todoListRepository.save(todoList);

    }

}
