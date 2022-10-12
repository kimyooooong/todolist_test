package com.todolist.repository;

import com.todolist.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users , Long> {
    Users findById(String id);
}
