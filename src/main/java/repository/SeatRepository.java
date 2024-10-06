package repository;

import domain.Seat;
import domain.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    // Showtime 객체를 기반으로 좌석 목록을 가져옴
    List<Seat> findByShowtime(Showtime showtime);
}
