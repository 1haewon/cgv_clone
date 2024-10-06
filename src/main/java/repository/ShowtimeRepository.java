package repository;

import domain.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByTheaterIdAndMovieId(Long theaterId, Long movieId);
}
