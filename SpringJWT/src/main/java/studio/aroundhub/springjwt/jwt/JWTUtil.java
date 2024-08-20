package studio.aroundhub.springjwt.jwt;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
// JWT 생성 및 검증
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    // 토큰요소 검증 기능
    // 이름
    public String getUsername(String token){

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }
    // 권한
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    // 시간
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                // 페이로드 부분에 데이터를 넣음
                .claim("username", username)
                .claim("role", role)
                // 현재 발행시간
                .issuedAt(new Date(System.currentTimeMillis()))
                // 소멸시간 설정
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                // 암호화 진행
                .signWith(secretKey)
                // 토큰을 컴팩 시켜서 리턴
                .compact();
    }
}

