package controller;

import DTO.ReservationRequestDTO;
import DTO.ReservationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ReservationService;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성 엔드포인트
    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO request) {
        Long reservationId = reservationService.createReservation(request);
        return ResponseEntity.ok("Reservation created successfully with ID: " + reservationId);
    }

    // 예약 조회 엔드포인트
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long reservationId) {
        ReservationResponseDTO reservation = reservationService.getReservationDetails(reservationId);
        return ResponseEntity.ok(reservation);
    }

    // 특정 사용자 ID로 예약 목록 조회 엔드포인트 (userId 타입을 String으로 변경)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReservationsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    // 예약 취소 엔드포인트
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();  // 성공 시 204 No Content 반환
    }
}
