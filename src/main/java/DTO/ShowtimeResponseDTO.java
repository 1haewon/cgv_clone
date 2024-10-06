package DTO;

import domain.Showtime;
import lombok.Getter;

@Getter
public class ShowtimeResponseDTO {
    private Long id;
    private String startTime;
    private String endTime;

    public ShowtimeResponseDTO(Showtime showtime) {
        this.id = showtime.getId();
        this.startTime = showtime.getStartTime();
        this.endTime = showtime.getEndTime();
    }
}
