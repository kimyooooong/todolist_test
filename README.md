## 프로젝트 소개

JAVA / SPRING BOOT / JPA 기반 TODO_LIST 관리 / 사용 API 구현

## 사용언어 및 프레임 워크

언어 : 
* Java 11 / Spring Boot 2.7.4

DB : 
* H2 ( JPA )

기타 :
기본 포트 8000 , 내장 톰캣사용.

코드 구조 및 서비스 흐름.
* 서비스에 회원 가입 및 로그인 탈퇴가 가능.
* 회원 가입 후 로그인 시 결과로 토큰을 받을 수 있음.
* 해당 토큰으로 TODO_LIST 작성 시 사용 됨.
* 회원 탈퇴 시 회원이 삭제 되고 모든 TODO_LIST 삭제 .
---

# 테스트 방법
```
1. 회원가입 ( POST ) - https://kimyong.kr/moais/swagger-ui/index.html#/users-controller/joinUsingPOST 
{
  "id": "string",
  "nickname": "string",
  "password": "string1234!!"
} 를 통해 회원 가입 가능.

2. 로그인 ( POST ) - https://kimyong.kr/moais/swagger-ui/index.html#/users-controller/loginUsingPOST
{
  "id": "string",
  "password": "string1234!!"
}
후 결과 값으로 토큰을 받아야함. 

결과 값 : 
{
  "success": true,
  "data": {
    "createdDate": "2022-10-13T10:05:01.316189",
    "modifiedDate": "2022-10-13T10:05:01.316189",
    "userId": 1,
    "id": "string",
    "nickName": "string",
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY1NjU1NTA3LCJleHAiOjE2NjU2NTczMDd9.MQOYekoj_jmExlCyDKVSPH2txMWYN9rS8oviWXFx6x4",
    "todoLists": []
  }
}

- jtwToken 값으로 TODO-LIST API Header 값에 넣어서 사용 가능.
- 회원 탈퇴 시 사용.

```
---
# 서버 실행 방법

## 1. 개인 서버로 접속 방법.

API 테스트 및 문서화 : https://kimyong.kr/moais/swagger-ui/index.html#/ <br>
H2 DB 접속 - https://kimyong.kr/moais/h2-console

## 2. 구동 방법 ( linux 기준 )

미리 빌드한 jar 파일로 서버 구동. ( [git 폴더 ] / result )

### Java - jdk 설치 ( 11 )
```
install java-11-openjdk-devel.x86_64
```

### git clone
```
git clone https://github.com/kimyooooong/todolist_test.git
```

### /result 폴더에 result.jar 실행
```
cd todolist_test/result
java -jar result.jar
```
## 3. 빌드 후 jar 파일로 실행 방법
```
./gradlew build 시 /build/libs 폴더에 todolist-0.0.1-SNAPSHOT.jar 파일 생성.
java -jar todolist-0.0.1-SNAPSHOT.jar
```
