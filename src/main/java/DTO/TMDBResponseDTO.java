package DTO;


import lombok.Data;

import java.util.List;

@Data
public class TMDBResponseDTO {
    private List<TMDBMovieDTO> results;

    @Data
    public static class TMDBMovieDTO {
        private Long id;
        private String title;
        private String releaseDate;
        private String overview;
        private String posterPath;
    }
}