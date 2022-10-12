package com.todolist.domain;


import com.todolist.enums.TodoListKind;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class TodoList extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @Enumerated(EnumType.STRING)
    private TodoListKind kind;

    private String title;

    private String desc;

}
