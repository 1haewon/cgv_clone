package DTO;

import lombok.Data;

import java.util.List;

@Data
public class ReservationRequestDTO {
    private Long theaterId;
    private Long movieId;
    private Long showtimeId;
    private Long userId;
    private List<Long> seats;
}
