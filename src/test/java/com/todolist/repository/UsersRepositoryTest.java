package com.todolist.repository;

import com.todolist.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private Users users;

    @BeforeEach
    void setUp() {
        users = Users.builder()
                .id("rladbdgns")
                .password("rlapass")
                .nickName("kimkimkim")
                .build();
    }

    @Test
    void testSaveUsers() {
        Users user = usersRepository.save(users);
        assertThat(user.getUserId(), is(1L));
        assertThat(users.getId(), is("rladbdgns"));
        assertThat(users.getPassword(), is("rlapass"));
        assertThat(users.getNickName(), is("kimkimkim"));
    }

    @Test
    void testGetUsers() {
        usersRepository.save(users);
        Optional<Users> result = usersRepository.findById(users.getUserId());
        assertThat(result.get().getId(), is("rladbdgns"));
    }

}
