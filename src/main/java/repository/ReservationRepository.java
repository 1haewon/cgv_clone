package repository;

import domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 사용자 ID로 예약 목록을 조회하는 메소드
    List<Reservation> findByUserId(Long userId);
}
