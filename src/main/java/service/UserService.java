package service;

import DTO.UserDTO;
import domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // 회원가입
    public UserDTO.UserResponse registerUser(UserDTO.UserCreateRequest request) {
        // 아이디 중복 확인
        if (userRepository.findByUserId(request.getId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 객체 생성 및 저장
        User user = new User(
                request.getId(), // 사용자가 제공한 id 필드
                request.getUser_name(),
                request.getEmail(),
                encodedPassword,
                request.getPhone_number(),
                request.getBirth_date(),
                request.getGender()
        );

        User savedUser = userRepository.save(user);

        return new UserDTO.UserResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getBirthDate(),
                savedUser.getGender()
        );
    }

    // 로그인 - JWT 발급
    public String login(UserDTO.UserLoginRequest request) {
        User user = userRepository.findByUserId(request.getId())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // JWT 발급
        String jwt = Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 만료시간 1일
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return jwt;
    }

    // 회원 삭제
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // 사용자 조회
    public UserDTO.UserResponse getUserById(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO.UserResponse(
                user.getUserId(),            // 사용자가 제공한 ID 반환
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getGender()
        );
    }

    // 사용자 정보 업데이트
    public UserDTO.UserResponse updateUser(String userId, UserDTO.UserCreateRequest request) {
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUser_name());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setPhoneNumber(request.getPhone_number());
        existingUser.setBirthDate(request.getBirth_date());
        existingUser.setGender(request.getGender());

        User updatedUser = userRepository.save(existingUser);

        return new UserDTO.UserResponse(
                updatedUser.getUserId(),      // 사용자가 제공한 ID 반환
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getPhoneNumber(),
                updatedUser.getBirthDate(),
                updatedUser.getGender()
        );
    }

    // UserDetailsService 구현
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}