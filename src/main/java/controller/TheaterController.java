package controller;

import DTO.SeatResponseDTO;
import DTO.TheaterResponseDTO;
import domain.Showtime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SeatService;
import service.ShowtimeService;
import service.TheaterService;

import java.util.List;

@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;

    // 극장 목록을 조회합니다.
    @GetMapping
    public ResponseEntity<List<TheaterResponseDTO>> getTheaters() {
        List<TheaterResponseDTO> theaters = theaterService.getTheaters();
        return ResponseEntity.ok(theaters);
    }

    // ID로 극장을 삭제합니다.
    @DeleteMapping("/{theaterId}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long theaterId) {
        theaterService.deleteTheater(theaterId);
        return ResponseEntity.ok("극장이 성공적으로 삭제되었습니다.");
    }

    // 특정 극장과 영화의 상영 시간을 조회합니다.
    @GetMapping("/{theaterId}/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimes(
            @PathVariable Long theaterId,
            @RequestParam Long movieId) {
        List<Showtime> showtimes = showtimeService.getShowtimesForTheaterAndMovie(theaterId, movieId);
        return ResponseEntity.ok(showtimes);
    }

    // 특정 상영 시간의 좌석 정보를 조회합니다.
    @GetMapping("/showtimes/{showtimeId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getSeatsForShowtime(@PathVariable Long showtimeId) {  // 반환 타입 수정
        List<SeatResponseDTO> seats = seatService.getSeats(showtimeId);  // Seat -> SeatResponseDTO로 변경
        return ResponseEntity.ok(seats);
    }

    // 특정 상영 시간에 좌석을 예약합니다.
    @PostMapping("/showtimes/{showtimeId}/seats")
    public ResponseEntity<String> reserveSeats(
            @PathVariable Long showtimeId,
            @RequestBody List<Long> seatIds) {
        seatService.reserveSeats(showtimeId, seatIds);
        return ResponseEntity.ok("좌석이 성공적으로 예약되었습니다.");
    }
}
