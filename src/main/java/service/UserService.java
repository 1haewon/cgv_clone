package service;

import DTO.UserDTO;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserDTO.UserResponse registerUser(UserDTO.UserCreateRequest request) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티로 변환
        User user = new User(
                request.getUser_name(),
                request.getEmail(),
                encodedPassword,
                request.getPhone_number(),
                request.getBirth_date(),
                request.getGender()
        );

        // 유저 정보 저장
        User savedUser = userRepository.save(user);

        // 응답 DTO 생성 후 반환
        return new UserDTO.UserResponse(
                savedUser.getUsername(),  // user_name
                savedUser.getEmail(),     // email
                savedUser.getPhoneNumber(), // phone_number
                savedUser.getBirthDate(),  // birth_date
                savedUser.getGender()     // gender
        );
    }

    // 로그인
    public boolean login(UserDTO.UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getId()).orElse(null);
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return true; // 로그인 성공
        }
        return false; // 로그인 실패
    }

    // 회원 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 사용자 조회
    public UserDTO.UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO.UserResponse(
                user.getUsername(),       // user_name
                user.getEmail(),          // email
                user.getPhoneNumber(),    // phone_number
                user.getBirthDate(),      // birth_date
                user.getGender()          // gender
        );
    }

    // 사용자 정보 업데이트
    public UserDTO.UserResponse updateUser(UserDTO.UserCreateRequest request) {
        User existingUser = userRepository.findByUsername(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUser_name());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setPhoneNumber(request.getPhone_number());
        existingUser.setBirthDate(request.getBirth_date());
        existingUser.setGender(request.getGender());

        User updatedUser = userRepository.save(existingUser);
        return new UserDTO.UserResponse(
                updatedUser.getUsername(),  // user_name
                updatedUser.getEmail(),     // email
                updatedUser.getPhoneNumber(), // phone_number
                updatedUser.getBirthDate(),  // birth_date
                updatedUser.getGender()     // gender
        );
    }
}
