package DTO;

import domain.Movie;
import lombok.Data;

@Data
public class MovieResponseDTO {
    private Long id;
    private String title;
    private String releaseDate;
    private String overview;
    private String posterPath;

    // Movie 엔티티를 DTO로 변환하는 생성자
    public MovieResponseDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.overview = movie.getOverview();
        this.posterPath = movie.getPosterPath();
    }
}