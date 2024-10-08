package controller;

import DTO.MovieRequestDTO;
import DTO.MovieResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.MovieService;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String apiKey;

    // 영화 상세 정보 조회 API
    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovieDetail(@PathVariable Long movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    // 기존 코드 유지
    @PostMapping
    public ResponseEntity<MovieResponseDTO> saveMovie(@RequestBody MovieRequestDTO request) {
        MovieResponseDTO savedMovie = movieService.saveMovie(request);
        return ResponseEntity.ok(savedMovie);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}