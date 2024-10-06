package service;

import io.jsonwebtoken.Claims;  // 추가: JWT 라이브러리의 Claims 클래스
import io.jsonwebtoken.Jwts;  // 추가: JWT 라이브러리의 Jwts 클래스
import io.jsonwebtoken.SignatureAlgorithm;  // 추가: JWT 서명 알고리즘 클래스
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtility {
    // 비밀 키 설정
    private String secret = "yourSecretKeyaokfosdfsoeifoifjosifjoisjdoifjosiejoijdovmldxggxxdgxd";
    private static final long EXPIRATION_TIME = 1000L * 60 * 60; // 1시간

    // 토큰 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)  // setSubject()에 매개변수로 받은 userId를 넣어 토큰의 주체로 설정
                .setIssuedAt(new Date())  // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))  // secret 값을 바이트로 변환 후 서명
                // JWT 서명 알고리즘 = HS512
                .compact();  // 최종적으로 JWT 생성
    }

    // 토큰 유효성 검사, 토큰에 관련된 사용자 또는 자원에 대한 정보를 제공
    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))  // 검증을 위해 서명 키 설정
                    .parseClaimsJws(token)  // 제공된 토큰을 넣어서 파싱하고 검증
                    .getBody();  // 검증이 성공하면 JWT에서 클레임을 추출
            return claims;
        } catch (Exception ex) {
            throw new RuntimeException("Invalid JWT Token", ex);  // 예외 처리
        }
    }
}
