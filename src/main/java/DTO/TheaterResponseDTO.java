package DTO;

import domain.Theater;
import lombok.Data;

@Data
public class TheaterResponseDTO {
    private Long id;
    private String name;
    private Long movieId;  // 영화 ID

    public TheaterResponseDTO(Theater theater) {
        this.id = theater.getId();
        this.name = theater.getName();
        this.movieId = movieId;  // 영화 ID
    }
}