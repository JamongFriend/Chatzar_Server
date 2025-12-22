package Project.Chatzar.Domain.auth;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findValidByMemberId(Long memberId);

    RefreshToken save(RefreshToken token);

    void deleteByMemberId(Long memberId);
}
