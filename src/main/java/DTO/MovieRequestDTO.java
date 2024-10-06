package DTO;

import lombok.Data;

import java.util.List;

@Data
public class MovieRequestDTO {
    private String title;
    private String posterPath;
    private String releaseDate;
    private String overview;
}
