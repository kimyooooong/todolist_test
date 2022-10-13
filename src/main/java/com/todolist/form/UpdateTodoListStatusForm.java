package com.todolist.form;

import com.todolist.enums.TodoListKind;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTodoListStatusForm {
    private TodoListKind todoListKind;
}
