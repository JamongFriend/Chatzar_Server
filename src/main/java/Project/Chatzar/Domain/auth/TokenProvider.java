package Project.Chatzar.Domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.aspectj.util.IStructureModel;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TokenProvider {
    private final SecretKey key;
    private final JwtProperties props;

    public TokenProvider(SecretKey key, JwtProperties props) {
        this.key = key;
        this.props = props;
    }

    public String createAccessToken(Long memberId, String email){
        Instant now = Instant.now();
        Instant exp = now.plus(props.accessTokenExpMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(Long memberId, String email){
        Instant now = Instant.now();
        Instant exp = now.plus(props.refreshTokenExpDays(), ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberId(String token) {
        return Long.parseLong(parse(token).getPayload().getSubject());
    }
}
