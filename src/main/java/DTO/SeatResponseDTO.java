package DTO;

import domain.Seat;
import lombok.Data;

@Data
public class SeatResponseDTO {
    private Long id;
    private String seatRow;
    private int seatNumber;  // number 대신 seatNumber로 수정
    private boolean reserved;

    public SeatResponseDTO(Seat seat) {
        this.id = seat.getId();
        this.seatRow = seat.getSeatRow();
        this.seatNumber = seat.getSeatNumber();  // seat.getNumber() 대신 seat.getSeatNumber() 호출
        this.reserved = seat.isReserved();
    }
}
