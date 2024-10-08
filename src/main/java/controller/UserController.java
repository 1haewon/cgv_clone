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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO.UserResponse> getUser(@PathVariable String userId) {
        UserDTO.UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody UserDTO.UserCreateRequest request) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/{userId}/reservations")
    public ResponseEntity<?> getUserReservations(@PathVariable String userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }
}
