package com.todolist.service;

import com.todolist.component.AES256;
import com.todolist.domain.Users;
import com.todolist.exception.ServiceException;
import com.todolist.repository.UsersRepository;
import com.todolist.security.JwtTokenProvider;
import com.todolist.utill.ValidationUtils;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsersService {

    private final UsersRepository usersRepository;

    private final AES256 aes256;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final EntityManager entityManager;

    /**
     * 회원가입 - ( 동시성 제어 )
     * @param id
     * @param password
     * @param nickName
     */
    public void join(String id , String password , String nickName) throws Exception {

        ValidationUtils.isIdPattern(id);
        ValidationUtils.isPasswordPattern(password);

        Optional<Users> user = usersRepository.findById(id);

        if(user.isPresent()){
            throw new ServiceException("이미 등록 된 아이디 입니다.");
        }
        usersRepository.save(
            Users.builder()
                    .id(aes256.encrypt(id))
                    .password(passwordEncoder.encode(password))
                    .nickName(aes256.encrypt(nickName))
                    .build());
    }

    /**
     * 로그인
     * @param id
     * @param password
     */
    public Users login(String id , String password) throws Exception {

        Users user = usersRepository.findById(aes256.encrypt(id)).orElseThrow(()->  new ServiceException("아이디 혹은 패스워드가 다릅니다."));

        if(!passwordEncoder.matches(password , user.getPassword())){
            throw new ServiceException("아이디 혹은 패스워드가 다릅니다.");
        }

        return getUsers(user.getUserId());
    }
    public Users getUsers(Long usersId) throws Exception {
        Users user = usersRepository.findById(usersId).orElseThrow(()->  new ServiceException("유저가 존재 하지 않습니다."));
        user.setId(aes256.decrypt(user.getId()));
        user.setNickName(aes256.decrypt(user.getNickName()));
        return user;
    }

//    public Users getUsers(String id) throws Exception {
//
//        Users user = usersRepository.findById(aes256.encrypt(id)).orElseThrow(()->  new ServiceException("유저가 존재 하지 않습니다."));
//        user.setId(aes256.decrypt(user.getId()));
//        user.setNickName(aes256.decrypt(user.getNickName()));
//        return user;
//    }

    public Users getOriginalUser(String id) throws Exception {
        return usersRepository.findById(aes256.encrypt(id)).orElseThrow(()->  new ServiceException("유저가 존재 하지 않습니다."));
    }

    public synchronized void out(Long id) throws Exception {
        Users user = getUsers(id);

        usersRepository.delete(user);

    }

}
