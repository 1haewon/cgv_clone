package service;

import DTO.UserDTO;
import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // 직접 주입

    // 회원가입
    public UserDTO.UserResponse registerUser(UserDTO.UserCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUser_name(),
                request.getEmail(),
                encodedPassword,
                request.getPhone_number(),
                request.getBirth_date(),
                request.getGender()
        );

        User savedUser = userRepository.save(user);
        return new UserDTO.UserResponse(
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getBirthDate(),
                savedUser.getGender()
        );
    }

    // 로그인
    public boolean login(UserDTO.UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getId()).orElse(null);
        return user != null && passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    // 회원 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 사용자 조회
    public UserDTO.UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO.UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getGender()
        );
    }

    // 사용자 정보 업데이트
    public UserDTO.UserResponse updateUser(Long userId, UserDTO.UserCreateRequest request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUser_name());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setPhoneNumber(request.getPhone_number());
        existingUser.setBirthDate(request.getBirth_date());
        existingUser.setGender(request.getGender());

        User updatedUser = userRepository.save(existingUser);
        return new UserDTO.UserResponse(
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getPhoneNumber(),
                updatedUser.getBirthDate(),
                updatedUser.getGender()
        );
    }

    // UserDetailsService 구현
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
