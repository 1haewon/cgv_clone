package service;

import DTO.SeatResponseDTO;
import domain.Seat;
import domain.Showtime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SeatRepository;
import repository.ShowtimeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    // 상영시간에 해당하는 좌석 조회
    public List<SeatResponseDTO> getSeats(Long showtimeId) {
        // showtimeId로 Showtime 객체를 가져옴
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found: " + showtimeId));

        // Showtime 객체를 사용하여 좌석을 조회
        List<Seat> seats = seatRepository.findByShowtime(showtime);

        // 좌석을 SeatResponseDTO로 변환하여 반환
        return seats.stream().map(SeatResponseDTO::new).collect(Collectors.toList());
    }

    // 좌석 예약 기능 (트랜잭션 처리 추가)
    @Transactional
    public void reserveSeats(Long showtimeId, List<Long> seatIds) {
        // 예약하려는 좌석 조회
        List<Seat> seatsToReserve = seatRepository.findAllById(seatIds);

        // 예약된 좌석이 있는지 사전 검사
        List<Seat> alreadyReservedSeats = seatsToReserve.stream()
                .filter(Seat::isReserved)
                .collect(Collectors.toList());

        // 만약 이미 예약된 좌석이 있다면 예외 처리
        if (!alreadyReservedSeats.isEmpty()) {
            throw new RuntimeException("Some seats are already reserved: " +
                    alreadyReservedSeats.stream()
                            .map(seat -> seat.getId().toString())
                            .collect(Collectors.joining(", ")));
        }

        // 좌석을 예약 상태로 변경
        for (Seat seat : seatsToReserve) {
            seat.setReserved(true);
        }

        // 변경된 좌석 정보 저장
        seatRepository.saveAll(seatsToReserve);
    }
}
