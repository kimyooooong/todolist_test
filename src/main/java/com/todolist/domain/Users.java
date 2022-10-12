package com.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Users extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique=true)
    private String id;

    @JsonIgnore
    private String password;

    private String nickName;


    @Builder
    public Users(String id ,String password , String nickName){
        this.id = id;
        this.password = password;
        this.nickName = nickName;

    }
}
