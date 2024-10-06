package service;

import DTO.MovieRequestDTO;
import DTO.MovieResponseDTO;
import domain.Movie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);  // Logger 추가
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;

    // TMDB API 관련 상수
    private final String TMDB_API_KEY = "87da660a1e3e808f42fd9eb6e0bf01d3";  // 실제 API 키
    private final String TMDB_API_URL = "https://api.themoviedb.org/3";  // TMDB API 기본 URL
    private final String TMDB_POPULAR_URL = TMDB_API_URL + "/movie/popular?api_key=" + TMDB_API_KEY + "&language=en-US&page=1";

    // 1. 인기 영화 목록 조회 (TMDB API에서 가져와서 저장 후 반환)
    public List<MovieResponseDTO> getPopularMovies() {
        // TMDB API 호출
        String response = restTemplate.getForObject(TMDB_POPULAR_URL, String.class);
        List<MovieResponseDTO> movieResponseDTOs = new ArrayList<>();

        try {
            // JSON 응답 처리
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            // TMDB API에서 가져온 데이터를 DTO로 변환하여 리스트에 저장
            for (JsonNode node : results) {
                Movie movie = new Movie();
                movie.setTitle(node.path("title").asText());
                movie.setPosterPath(node.path("poster_path").asText());
                movie.setReleaseDate(node.path("release_date").asText());
                movie.setOverview(node.path("overview").asText());

                // 추가: overview 길이 로그 출력
                logger.info("Overview length: " + movie.getOverview().length());

                // 데이터베이스에 영화 저장
                movieRepository.save(movie);

                // MovieResponseDTO로 변환하여 리스트에 추가
                movieResponseDTOs.add(new MovieResponseDTO(movie));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieResponseDTOs;
    }

    // 2. 최신 영화 목록 조회 (현재는 데이터베이스에서만 가져옴)
    public List<MovieResponseDTO> getLatestMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(MovieResponseDTO::new).collect(Collectors.toList());
    }

    // 3. 영화 상세 정보 조회
    public MovieResponseDTO getMovieDetails(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return new MovieResponseDTO(movie);
    }

    // 4. 영화 저장
    public void saveMovie(MovieRequestDTO request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setPosterPath(request.getPosterPath());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setOverview(request.getOverview());

        // 추가: overview 길이 로그 출력
        logger.info("Overview length: " + movie.getOverview().length());

        movieRepository.save(movie);
    }

    // 5. 영화 삭제
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    // 6. 최신 영화 데이터 갱신 (TMDB API에서 가져와 데이터베이스에 저장)
    public void updateLatestMovies() {
        // TMDB API 호출 URL
        String url = TMDB_API_URL + "/movie/latest?api_key=" + TMDB_API_KEY + "&language=en-US";

        // TMDB API에서 최신 영화 데이터를 가져옴
        MovieResponseDTO latestMovieResponse = restTemplate.getForObject(url, MovieResponseDTO.class);

        if (latestMovieResponse != null) {
            Long movieId = latestMovieResponse.getId();
            // 영화 ID로 기존 데이터베이스에서 영화 조회
            Movie existingMovie = movieRepository.findById(movieId).orElse(null);

            if (existingMovie == null) {
                // 영화가 없는 경우 새로 추가
                Movie newMovie = new Movie();
                newMovie.setId(latestMovieResponse.getId());
                newMovie.setTitle(latestMovieResponse.getTitle());
                newMovie.setReleaseDate(latestMovieResponse.getReleaseDate());
                newMovie.setOverview(latestMovieResponse.getOverview());
                newMovie.setPosterPath(latestMovieResponse.getPosterPath());

                // 데이터베이스에 저장
                movieRepository.save(newMovie);
            } else {
                // 영화가 이미 존재하는 경우 업데이트
                existingMovie.setTitle(latestMovieResponse.getTitle());
                existingMovie.setReleaseDate(latestMovieResponse.getReleaseDate());
                existingMovie.setOverview(latestMovieResponse.getOverview());
                existingMovie.setPosterPath(latestMovieResponse.getPosterPath());

                // 데이터베이스 업데이트
                movieRepository.save(existingMovie);
            }
        }
    }
}
