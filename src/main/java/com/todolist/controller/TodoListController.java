package com.todolist.controller;


import com.todolist.common.RestResponse;
import com.todolist.domain.Users;
import com.todolist.enums.TodoListKind;
import com.todolist.form.JoinForm;
import com.todolist.form.TodoListForm;
import com.todolist.form.UpdateTodoListStatusForm;
import com.todolist.security.JwtTokenProvider;
import com.todolist.service.TodoListService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/todolists")
@RequiredArgsConstructor
@Log4j2
public class TodoListController {


    private final TodoListService todoListService;


    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation("토큰 인증 필요 - TODO List 조회 ( 가장 최근 1 개 ) ")
    @GetMapping("/recent")
    public ResponseEntity<RestResponse> getTodoListOne(@RequestHeader("Authentication") String Header) {

        jwtTokenProvider.getAuthentication(Header).getPrincipal();

        return ResponseEntity.ok(RestResponse.ok(todoListService.getTodoListRecent()));
    }

    @ApiOperation("토큰 인증 필요 - TODO List 조회 ( 전체 목록 페이지네이션 ( 최신순 정렬 ) ) ")
    @GetMapping("/all")
    public ResponseEntity<RestResponse> getTodoListPage(
            @RequestHeader("Authentication") String Header,
            @RequestParam(value = "page" , defaultValue = "1") Integer page,
            @RequestParam(value = "size" , defaultValue = "20") Integer size) {

        jwtTokenProvider.getAuthentication(Header).getPrincipal();

        return ResponseEntity.ok(RestResponse.ok(todoListService.getTodoListAll( PageRequest.of(page-1 , size , Sort.by("todoId").descending()))));
    }


    @ApiOperation("토큰 인증 필요 - TODO 리스트 작성.")
    @PostMapping("/add-todolist")
    public ResponseEntity<RestResponse> addTodoList(
            @RequestHeader("Authentication") String Header,
            @RequestBody TodoListForm todoListForm) throws Exception {

        log.info("Authentication : {} " , Header);

        Users users = (Users) jwtTokenProvider.getAuthentication(Header).getPrincipal();

        return ResponseEntity.ok(RestResponse.ok(todoListService.addTodoList(users.getUserId(),todoListForm.getTitle(),todoListForm.getDesc())));
    }


    @ApiOperation("토큰 인증 필요 - TODO 리스트 상태 수정")
    @PutMapping("/{todolistId}/status")
    public ResponseEntity<RestResponse> putStatus(
            @RequestHeader("Authentication") String Header,
            @PathVariable Long todolistId,
            @ApiParam("TODO - 할 일, ING - 진행중,COMP - 완료 됨 ")
            @RequestBody UpdateTodoListStatusForm updateTodoListStatusForm) {

        //토큰인증.
        jwtTokenProvider.getAuthentication(Header);
        todoListService.updateStatusTodoList(todolistId ,updateTodoListStatusForm.getTodoListKind());

        return ResponseEntity.ok(RestResponse.ok());
    }


}
