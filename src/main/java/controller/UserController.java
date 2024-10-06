package controller;

import DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import service.ReservationService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReservationService reservationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO.UserCreateRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO.UserLoginRequest loginRequest) {
        boolean isLoginSuccessful = userService.login(loginRequest);
        if (isLoginSuccessful) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO.UserResponse> getUser(@PathVariable Long userId) {
        UserDTO.UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO.UserCreateRequest request) {
        userService.updateUser(request);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/{userId}/reservations")
    public ResponseEntity<?> getUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }


}
