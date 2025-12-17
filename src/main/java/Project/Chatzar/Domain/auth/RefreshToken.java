package Project.Chatzar.Domain.auth;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Instant expiresAt;

    protected RefreshToken() {}

    public RefreshToken(String token, Long memberId, Instant expiresAt) {
        this.token = token;
        this.memberId = memberId;
        this.expiresAt = expiresAt;
    }
}
