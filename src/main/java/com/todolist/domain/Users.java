package com.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Users extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique=true)
    @Setter
    private String id;

    @JsonIgnore
    private String password;

    @Setter
    private String nickName;

    @Setter
    @Transient
    private String jwtToken;

    @JsonManagedReference
    @OneToMany(mappedBy = "users" , fetch = FetchType.LAZY)
    private List<TodoList> todoLists = new ArrayList<>();

    @Builder
    public Users(String id ,String password , String nickName){
        this.id = id;
        this.password = password;
        this.nickName = nickName;

    }
}
