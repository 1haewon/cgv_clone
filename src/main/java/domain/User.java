package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String userId;  // 사용자가 제공하는 ID (직접 입력받음)

    @Column(nullable = false)
    private String username;  // 사용자 이름

    @Column(nullable = false)
    private String email;  // 이메일 주소

    @Column(nullable = false)
    private String password;  // 비밀번호

    @Column(nullable = false)
    private String phoneNumber;  // 전화번호

    @Column(nullable = false)
    private String birthDate;  // 생년월일

    @Column(nullable = false)
    private String gender;  // 성별

    // 생성자
    public User(String userId, String username, String email, String password, String phoneNumber, String birthDate, String gender) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
    }
}
