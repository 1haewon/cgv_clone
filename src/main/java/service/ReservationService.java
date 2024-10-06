package service;

import DTO.ReservationRequestDTO;
import DTO.ReservationResponseDTO;
import domain.Movie;
import domain.Reservation;
import domain.Seat;
import domain.Showtime;
import domain.Theater;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;

    // 예약 생성 메소드
    public Long createReservation(ReservationRequestDTO request) {
        // 1. 선택한 좌석 정보 가져오기
        List<Seat> seats = seatRepository.findAllById(request.getSeats());

        // 좌석 정보 로깅
        logger.info("선택한 좌석들: {}", seats);

        // 2. 좌석이 이미 예약되었는지 체크
        for (Seat seat : seats) {
            if (seat.isReserved()) {
                throw new RuntimeException("이미 예약된 좌석입니다: " + seat.getId());
            }
        }

        // 3. 영화, 극장, 상영 시간 정보 가져오기
        logger.info("영화 ID: {}, 극장 ID: {}, 상영 시간 ID: {}", request.getMovieId(), request.getTheaterId(), request.getShowtimeId());

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> {
                    logger.error("영화를 찾을 수 없습니다. Movie ID: {}", request.getMovieId());
                    return new RuntimeException("영화를 찾을 수 없습니다.");
                });

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> {
                    logger.error("극장을 찾을 수 없습니다. Theater ID: {}", request.getTheaterId());
                    return new RuntimeException("극장을 찾을 수 없습니다.");
                });

        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> {
                    logger.error("상영시간을 찾을 수 없습니다. Showtime ID: {}", request.getShowtimeId());
                    return new RuntimeException("상영시간을 찾을 수 없습니다.");
                });

        // 4. 예약 생성 및 정보 설정
        Reservation reservation = new Reservation();
        reservation.setTheater(theater); // 극장 정보 설정
        reservation.setMovie(movie); // 영화 정보 설정
        reservation.setShowtime(showtime); // 상영시간 정보 설정
        reservation.setUserId(request.getUserId()); // 사용자 ID 설정
        reservation.setMovieTitle(movie.getTitle());  // 영화 제목 설정
        reservation.setTheaterName(theater.getName());  // 극장 이름 설정

        // 5. 좌석 번호를 콤마로 구분하여 문자열로 설정 (좌석 행과 번호를 결합)
        String seatNumbers = seats.stream()
                .map(seat -> seat.getSeatRow() + seat.getSeatNumber()) // 좌석 행과 번호 결합
                .collect(Collectors.joining(", ")); // 좌석 번호들을 콤마로 구분하여 문자열로 합침
        reservation.setSeatNumber(seatNumbers);  // 좌석 번호 저장
        logger.info("선택된 좌석 번호: {}", seatNumbers);  // 좌석 번호 로깅

        // 6. 먼저 예약을 저장하여 ID 생성
        Reservation savedReservation = reservationRepository.save(reservation);

        // 7. 좌석과 저장된 예약을 연관 설정한 후 좌석 저장
        for (Seat seat : seats) {
            seat.setReserved(true); // 좌석 예약 상태 설정
            seat.setReservation(savedReservation); // 좌석과 저장된 예약 연관 설정
        }
        seatRepository.saveAll(seats); // 좌석 상태 저장

        logger.info("예약 생성 완료, 예약 ID: {}", savedReservation.getId());

        // 8. 예약 ID 반환
        return savedReservation.getId();
    }

    // 예약 상세 정보 조회 메소드
    public ReservationResponseDTO getReservationDetails(Long reservationId) {
        // 예약 정보를 가져옵니다.
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (reservationOptional.isEmpty()) {
            throw new RuntimeException("예약을 찾을 수 없습니다: " + reservationId);
        }

        Reservation reservation = reservationOptional.get();

        // 예약 정보를 DTO로 변환하여 반환
        return new ReservationResponseDTO(reservation);
    }

    // 특정 사용자 ID로 예약 목록 조회 메소드 추가
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    // 예약 취소 메소드 추가 (완전 삭제 방식)
    public void cancelReservation(Long reservationId) {
        // 예약 정보 가져오기
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다: " + reservationId));

        // 좌석 예약 상태 해제
        List<Seat> seats = reservation.getSeats();
        for (Seat seat : seats) {
            seat.setReserved(false);  // 좌석 예약 상태 해제
            seat.setReservation(null);  // 좌석과 예약의 연관 해제
        }

        seatRepository.saveAll(seats);  // 좌석 정보 업데이트

        // 예약 삭제
        reservationRepository.deleteById(reservationId);
        logger.info("예약이 성공적으로 취소되었습니다. 예약 ID: {}", reservationId);
    }
}
