package Project.Chatzar.Domain.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import Project.Chatzar.presentation.dto.auth.JwtProperties;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessExpMillis;
    private final long refreshExpMillis;
    private final String issuer;

    public JwtTokenProvider(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        this.accessExpMillis = Duration.ofMinutes(props.accessTokenExpMinutes()).toMillis();
        this.refreshExpMillis = Duration.ofDays(props.refreshTokenExpDays()).toMillis();
        this.issuer = props.issuer();
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
                .setIssuer(issuer)
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
