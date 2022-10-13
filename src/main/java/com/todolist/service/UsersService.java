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

    /**
     * 회원가입 - ( 동시성 제어 ) ( 패스워드 단방향 SHA-256 , 그 외 정보 AES-256 양방향 암호화 )
     * @param id - 아이디
     * @param password - 패스워드
     * @param nickName - 닉네임
     */
    public synchronized Users join(String id , String password , String nickName) throws Exception {

        ValidationUtils.isIdPattern(id);
        ValidationUtils.isPasswordPattern(password);

        Optional<Users> user = usersRepository.findById(aes256.encrypt(id));

        if(user.isPresent()){
            throw new ServiceException("이미 등록 된 아이디 입니다.");
        }
        return usersRepository.save(
            Users.builder()
                    .id(aes256.encrypt(id))
                    .password(passwordEncoder.encode(password))
                    .nickName(aes256.encrypt(nickName))
                    .build());
    }

    /**
     * 로그인 시 JWT 토큰 발급.
     * @param id - 아이디
     * @param password - 패스워드
     */
    public Users login(String id , String password) throws Exception {

        Users user = usersRepository.findById(aes256.encrypt(id)).orElseThrow(()->  new ServiceException("아이디 혹은 패스워드가 다릅니다."));

        if(!passwordEncoder.matches(password , user.getPassword())){
            throw new ServiceException("아이디 혹은 패스워드가 다릅니다.");
        }

        return getUsers(user.getUserId());
    }

    /**
     * 유저 정보 가져오기 ( 복호화 )
     * @param usersId - LONG 아이디 기반. ( 고유 정보 )
     * @return
     * @throws Exception
     */
    public Users getUsers(Long usersId) throws Exception {
        Users user = usersRepository.findById(usersId).orElseThrow(()->  new ServiceException("유저가 존재 하지 않습니다."));
        user.setId(aes256.decrypt(user.getId()));
        user.setNickName(aes256.decrypt(user.getNickName()));
        return user;
    }

    /**
     * 유저 정보 가져오기 ( 비 복호화 )
     * @param id - String 아이디 기반.
     * @return
     * @throws Exception
     */
    public Users getOriginalUser(String id) throws Exception {
        return usersRepository.findById(aes256.encrypt(id)).orElseThrow(()->  new ServiceException("유저가 존재 하지 않습니다."));
    }

    /**
     * 회원 탈퇴 (토큰인증 필요 ) ( 동시성 제어 )
     * @param id - 탈퇴 할 아이디 ( 고유정보 )
     * @throws Exception
     */
    public synchronized void out(Long id) throws Exception {
        usersRepository.delete(getUsers(id));
    }

}
