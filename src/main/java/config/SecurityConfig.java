package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import service.UserService;  // UserService를 직접 사용

@Configuration
public class SecurityConfig {

    private final UserService userService;  // UserService 주입

    // 생성자를 통해 UserService 주입
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable() // CORS 허용 및 CSRF 비활성화
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/movies/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    // PasswordEncoder Bean 정의 (SecurityConfig에서만 관리)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 비밀번호 인코더 사용
    }

    // UserService에서 UserDetailsService 구현하는 경우, 이를 사용하여 사용자 정보 로드
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;  // UserService에서 UserDetailsService 구현
    }
}

