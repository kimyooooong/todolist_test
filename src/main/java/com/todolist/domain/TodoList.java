package com.todolist.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.enums.TodoListKind;
import lombok.*;

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
    @Setter
    private TodoListKind kind;

    private String title;

    private String desc;

    @ManyToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
    @JoinColumn(name ="users_id", insertable = false, updatable = false)
    @JsonBackReference
    private Users users;

    @Column(name = "users_id")
    private Long usersId;


    @Builder
    public TodoList(String title , String desc , Long userId , TodoListKind kind){
        this.title = title;
        this.desc = desc;
        this.usersId = userId;
        this.kind = kind;
    }

}
