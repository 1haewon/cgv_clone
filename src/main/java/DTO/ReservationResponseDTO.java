package DTO;

import domain.Reservation;
import domain.Seat;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReservationResponseDTO {
    private Long reservationId;
    private MovieDTO movie;
    private TheaterDTO theater;
    private ShowtimeDTO showtime;
    private List<SeatDTO> seats;
    private int totalPrice;
    private String status;

    // 생성자
    public ReservationResponseDTO(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.movie = new MovieDTO(reservation.getMovie().getId(), reservation.getMovie().getTitle());
        this.theater = new TheaterDTO(reservation.getTheater().getId(), reservation.getTheater().getName());
        this.showtime = new ShowtimeDTO(reservation.getShowtime().getStartTime(), reservation.getShowtime().getEndTime());
        this.seats = reservation.getSeats().stream()
                .map(seat -> new SeatDTO(seat.getSeatRow(), seat.getSeatNumber()))
                .collect(Collectors.toList());
        this.totalPrice = calculateTotalPrice(reservation.getSeats());
        this.status = "예약 완료";
    }

    private int calculateTotalPrice(List<Seat> seats) {
        return seats.size() * 10000;  // 좌석 당 10,000원으로 가격 계산
    }

    @Data
    public static class MovieDTO {
        private Long id;
        private String title;

        public MovieDTO(Long id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    @Data
    public static class TheaterDTO {
        private Long id;
        private String name;

        public TheaterDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    public static class ShowtimeDTO {
        private String startTime;
        private String endTime;

        public ShowtimeDTO(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Data
    public static class SeatDTO {
        private String row;
        private int number;

        public SeatDTO(String row, int number) {
            this.row = row;
            this.number = number;
        }
    }
}
