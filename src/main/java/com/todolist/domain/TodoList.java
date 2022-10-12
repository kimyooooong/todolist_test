package com.todolist.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.enums.TodoListKind;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="users_id")
    @JsonBackReference
    private Users users;

    @Builder
    public TodoList(String title , String desc , Users users){
        this.title = title;
        this.desc = desc;
        this.users = users;
    }

}
