package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTO {

    // 회원 가입 - 받아오는 요청 정보
    @Data
    public static class UserCreateRequest {
        private String id;            // 기존 userId -> id로 수정
        private String user_name;      // name -> user_name으로 수정
        private String password;
        private String email;
        private String phone_number;   // phoneNumber -> phone_number로 수정
        private String birth_date;     // birthDate -> birth_date로 수정
        private String gender;
    }

    // 로그인 - 받아오는 요청 정보
    @Data
    public static class UserLoginRequest {
        private String id;             // 기존 userId -> id로 수정
        private String password;
    }

    // 응답 정보
    @Data
    @AllArgsConstructor
    public static class UserResponse {
        private String user_name;
        private String email;
        private String phone_number;
        private String birth_date;
        private String gender;
    }
}
