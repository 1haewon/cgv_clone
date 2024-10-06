package DTO;

import lombok.Data;

import java.util.List;

public class ReservationDTO {

    @Data
    public static class ReservationResponse {
        private Long reservationId;
        private MovieDTO movie;
        private TheaterDTO theater;
        private ShowtimeDTO showtime;
        private List<SeatDTO> seats;
        private int totalPrice;
        private String status;
    }

    @Data
    public static class MovieDTO {
        private Long id;
        private String title;
    }

    @Data
    public static class TheaterDTO {
        private Long id;
        private String name;
    }

    @Data
    public static class ShowtimeDTO {
        private String startTime;
        private String endTime;
    }

    @Data
    public static class SeatDTO {
        private String row;
        private int number;
    }
}
