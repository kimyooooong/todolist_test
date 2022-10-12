package com.todolist.service;


import com.todolist.domain.TodoList;
import com.todolist.domain.Users;
import com.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    private final UsersService usersService;

    /**
     *
     * @return
     */
    public TodoList addTodoList(Long usersId , String title , String desc) throws Exception {

        Users users = usersService.getUsers(usersId);

        return todoListRepository.save(TodoList.builder()
                .title(title)
                .desc(desc)
                .users(users)
                .build());
    }

}
