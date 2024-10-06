package controller;

import DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // 회원가입 API 엔드포인트
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO.UserCreateRequest userCreateRequest) {
        userService.registerUser(userCreateRequest);  // DTO를 넘겨줍니다.
        return ResponseEntity.ok("User registered successfully");
    }
}
