package com.todolist.utill;

import com.todolist.exception.ServiceException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidPhoneNumber(String number) {
        if (!StringUtils.hasText(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

//    public static boolean isValidEmailAddress(String email) {
//        boolean result = true;
//        try {
//            InternetAddress emailAddr = new InternetAddress(email);
//            emailAddr.validate();
//        } catch (AddressException ex) {
//            result = false;
//        }
//        return result;
//    }

//    public static void memberJoinValidation(Member member) {
//        if (!ValidationUtils.isValidPhoneNumber(member.getPhone())) {
//            throw new IllegalArgumentException("핸드폰 번호 형식이 올바르지 않습니다.");
//        }
//        if (!ValidationUtils.isValidEmailAddress(member.getEmail())) {
//            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다");
//        }
//    }

    public static void isIdPattern(String id) {
        String idPattern1 = "^(?=.*[0-9]).{5,20}$"; // 숫자조합
        String idPattern2 = "^(?=.*[a-z]).{5,20}$"; // 영어소문자조합
        Matcher matcher1 = Pattern.compile(idPattern1).matcher(id);
        Matcher matcher2 = Pattern.compile(idPattern2).matcher(id);

        if (!matcher1.matches() && !matcher2.matches()) {
            throw new ServiceException("아이디는 5~20자 사이의 영어 소문자 혹은 숫자만의 조합을 사용해주세요.");
        }
    }

    public static void isPasswordPattern(String password) {
        String passwordPattern1 = "^(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; // 영어대소문자 + 특수문자
        String passwordPattern2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; // 영어대소문자 + 숫자
        String passwordPattern3 = "^(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,20}$"; // 특수문자 + 숫자

        Matcher matcher1 = Pattern.compile(passwordPattern1).matcher(password);
        Matcher matcher2 = Pattern.compile(passwordPattern2).matcher(password);
        Matcher matcher3 = Pattern.compile(passwordPattern3).matcher(password);

        if (!matcher1.matches() && !matcher2.matches() && !matcher3.matches())
            throw new ServiceException("비밀번호는 8~20자 사이의 영어대소문자, 숫자, 특수문자 조합중 최소 2가지 조합을 사용해주세요.");
    }
}