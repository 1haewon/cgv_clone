package controller;

import DTO.SeatResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SeatService;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // 특정 상영 시간의 좌석 조회
    @GetMapping("/{showtimeId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getSeats(@PathVariable Long showtimeId) {
        List<SeatResponseDTO> seats = seatService.getSeats(showtimeId);
        return ResponseEntity.ok(seats);
    }

    // 특정 상영 시간에 좌석 예약
    @PostMapping("/{showtimeId}/seats")
    public ResponseEntity<String> reserveSeats(
            @PathVariable Long showtimeId,
            @RequestBody List<Long> seatIds) {  // 수정된 부분
        seatService.reserveSeats(showtimeId, seatIds);  // 수정된 부분
        return ResponseEntity.ok("Seats reserved successfully");
    }

}
