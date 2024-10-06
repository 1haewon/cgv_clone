package service;

import DTO.SeatResponseDTO;
import DTO.ShowtimeResponseDTO;
import DTO.TheaterResponseDTO;
import domain.Seat;
import domain.Showtime;
import domain.Theater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.SeatRepository;
import repository.ShowtimeRepository;
import repository.TheaterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    // 1. 극장 목록 조회
    public List<TheaterResponseDTO> getTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream().map(TheaterResponseDTO::new).collect(Collectors.toList());
    }

    // 2. 극장 삭제
    public void deleteTheater(Long theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    // 3. 특정 극장의 상영시간 조회 (영화 ID로 필터링)
    public List<ShowtimeResponseDTO> getShowtimesByTheaterAndMovie(Long theaterId, Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByTheaterIdAndMovieId(theaterId, movieId);
        return showtimes.stream().map(ShowtimeResponseDTO::new).collect(Collectors.toList());
    }

    // 4. 특정 상영시간의 좌석 조회
    public List<SeatResponseDTO> getSeatsByShowtime(Long showtimeId) {
        // showtimeId로 Showtime 객체를 조회
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found: " + showtimeId));

        // Showtime 객체를 사용해 좌석 조회
        List<Seat> seats = seatRepository.findByShowtime(showtime);
        return seats.stream().map(SeatResponseDTO::new).collect(Collectors.toList());
    }

    // 5. 특정 상영시간의 좌석 예약
    public void reserveSeats(Long showtimeId, List<Long> seatIds) {
        List<Seat> seatsToReserve = seatRepository.findAllById(seatIds);
        for (Seat seat : seatsToReserve) {
            if (!seat.isReserved()) {
                seat.setReserved(true);
            } else {
                throw new RuntimeException("Seat already reserved: " + seat.getId());
            }
        }
        seatRepository.saveAll(seatsToReserve);
    }


}
