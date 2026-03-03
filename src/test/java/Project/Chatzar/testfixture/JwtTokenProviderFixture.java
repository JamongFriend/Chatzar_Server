package Project.Chatzar.testfixture;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtTokenProviderFixture {
    private final Key key;

    public JwtTokenProviderFixture(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createExpiredToken(Long memberId, String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000); // 1초 전으로 설정해서 즉시 만료시킴

        return Jwts.builder()
                .setSubject(email)
                .claim("memberId", memberId)
                .setIssuedAt(now)
                .setExpiration(validity) // 과거 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createInvalidSignatureToken(Long memberId, String email) {
        Key wrongKey = Keys.hmacShaKeyFor("wrongSecretKeyWRONGSECRETKEY12345678".getBytes());
        return Jwts.builder()
                .claim("memberId", memberId)
                .setSubject(email)
                .signWith(wrongKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
