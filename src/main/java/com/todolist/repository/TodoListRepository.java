package com.todolist.repository;

import com.todolist.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList , Long> {
}
