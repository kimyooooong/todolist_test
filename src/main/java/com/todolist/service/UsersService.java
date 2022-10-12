package com.todolist.service;

import com.todolist.component.AES256;
import com.todolist.domain.Users;
import com.todolist.exception.ServiceException;
import com.todolist.repository.UsersRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 회원 가입
     * @param password
     * @param nickName
     */
    public void join(String id , String password , String nickName){
        try {
            usersRepository.save(
                    Users.builder()
                        .id(id)
                        .password(passwordEncoder.encode(password))
                        .nickName(nickName)
                        .build());
        } catch (Exception e) {
            throw new ServiceException("ID 가 중복 되었습니다.");
        }
    }

    /**
     * 로그인
     * @param id
     * @param password
     */
    public Users login(String id , String password){

        Users user = usersRepository.findById(id);

        if(user == null){
            throw new ServiceException("아이디 혹은 패스워드가 다릅니다.");
        }

        if(!passwordEncoder.matches(password , user.getPassword())){
            throw new ServiceException("아이디 혹은 패스워드가 다릅니다.222");
        }

        return user;
    }
    public void out(Long id){
        Optional<Users> user = usersRepository.findById(id);

        if( user.isEmpty() ){
            throw new ServiceException("아이디가 존재하지 않습니다.");
        }

        usersRepository.delete(user.get());

    }

}
