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

    /**
     * 최근 작성된 TODO LIST 1개
     * @return
     */
    public TodoList getTodoListRecent(){
        return todoListRepository.findOneOrderByTodoIdOrderDesc().orElseThrow(()->  new ServiceException("TODO 리스트가 존재 하지 않습니다."));
    }

    /**
     * TODO LIST 전제조회 - 페이지네이션 ( 최신순 정렬 )
     * @return
     */
    public List<TodoList> getTodoListAll(Pageable pageable){
        return todoListRepository.findAll(pageable).getContent();
    }

    /**
     * TODO LIST 생성 - 기본 상태값 TODO
     * @param userId - 작성하는 유저 아이디 ( 로그인 )
     * @param title - TODO LIST 타이틀
     * @param desc - TODO LIST DESC
     * @return - 작성된 TODO LIST
     * @throws Exception
     */
    @Transactional
    public TodoList addTodoList(Long userId, String title , String desc) {

        return todoListRepository.save(TodoList.builder()
                .title(title)
                .desc(desc)
                .userId(userId)
                .kind(TodoListKind.TODO)
                .build());
    }

    /**
     * TODO LIST 상태 변경
     * @param todoListId - 상태 변경 할 TODO LIST ID
     * @param todoListKind - 변경 할 상태 ( TODO ,ING , COMP )
     */
    @Transactional
    public void updateStatusTodoList(Long todoListId , TodoListKind todoListKind){

        TodoList todoList = todoListRepository.findById(todoListId).orElseThrow(()->  new ServiceException("TODO 리스트가 존재 하지 않습니다."));
        todoList.setKind(todoListKind);
        todoListRepository.save(todoList);

    }

}
