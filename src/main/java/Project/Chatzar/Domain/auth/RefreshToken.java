package Project.Chatzar.Domain.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens", indexes = @Index(name = "idx_refresh_member", columnList = "memberId", unique = true))
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String refreshTokenHash;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Builder
    public RefreshToken(Long memberId, String refreshTokenHash, LocalDateTime expiresAt) {
        this.memberId = memberId;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public void revoke() {
        this.revoked = true;
    }
}
