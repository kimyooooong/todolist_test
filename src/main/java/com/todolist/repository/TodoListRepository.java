package com.todolist.repository;

import com.todolist.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList , Long> {

    @Query(value = "SELECT * FROM TODO_LIST ORDER BY TODO_ID DESC LIMIT 1", nativeQuery = true)
    Optional<TodoList> findOneOrderByTodoIdOrderDesc();
}
