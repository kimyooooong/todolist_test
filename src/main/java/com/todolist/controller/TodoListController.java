package com.todolist.controller;


import com.todolist.common.RestResponse;
import com.todolist.form.JoinForm;
import com.todolist.service.TodoListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todolist")
@RequiredArgsConstructor
@Log4j2
public class TodoListController {


    private final TodoListService todoListService;

    @PostMapping("/users/{usersId}/add-todolist")
    public ResponseEntity<RestResponse> addTodoList(
            @PathVariable Long userId,
            @RequestBody JoinForm joinForm
    )  {

        return ResponseEntity.ok(RestResponse.ok(todoListService.addTodoList(null,null,null)));
    }



}
