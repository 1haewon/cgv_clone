package controller;

import DTO.MovieRequestDTO;
import DTO.MovieResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/popular")
    public ResponseEntity<List<MovieResponseDTO>> getPopularMovies() {
        List<MovieResponseDTO> movies = movieService.getPopularMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<MovieResponseDTO>> getLatestMovies() {
        List<MovieResponseDTO> movies = movieService.getLatestMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponseDTO> getMovieDetails(@PathVariable Long movieId) {
        MovieResponseDTO movie = movieService.getMovieDetails(movieId);
        return ResponseEntity.ok(movie);
    }

    @PostMapping
    public ResponseEntity<String> saveMovie(@RequestBody MovieRequestDTO request) {
        movieService.saveMovie(request);
        return ResponseEntity.ok("Movie saved successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMovie(@RequestParam Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok("Movie deleted successfully");
    }

}