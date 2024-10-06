package service;

import domain.Showtime;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShowtimeService {
    // 상영 시간 정보를 처리하는 메소드
    public List<Showtime> getShowtimesForTheaterAndMovie(Long theaterId, Long movieId) {
        // 실제 상영시간 처리 로직
        return null; // 임시 코드
    }
}
