package Project.Chatzar.Domain.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessExpMillis;
    private final long refreshExpMillis;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.access-exp-min}") long accessExpMin,
                            @Value("${jwt.refresh-exp-days}") long refreshExpDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMillis = Duration.ofMinutes(accessExpMin).toMillis();
        this.refreshExpMillis = Duration.ofDays(refreshExpDays).toMillis();
    }

    public String createAccessToken(Long memberId, String email) {
        return createToken(memberId, email, accessExpMillis);
    }

    public String createRefreshToken(Long memberId, String email) {
        return createToken(memberId, email, refreshExpMillis);
    }

    private String createToken(Long memberId, String email, long expMillis) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValid(String token) {
        try {
            parse(token); // 서명/만료/형식 검증까지 여기서 다 걸림
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberId(String token) {
        return Long.parseLong(parse(token).getBody().getSubject());
    }

    public String getEmail(String token) {
        Object v = parse(token).getBody().get("email");
        return v == null ? null : v.toString();
    }
}
