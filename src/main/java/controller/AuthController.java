package controller;

import DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public UserDTO.UserResponse registerUser(@RequestBody UserDTO.UserCreateRequest request) {
        return userService.registerUser(request);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO.UserLoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }
}
