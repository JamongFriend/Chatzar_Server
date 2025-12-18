package Project.Chatzar.Domain.auth;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findValidByMemberId(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    RefreshToken save(RefreshToken token);

    void deleteByMemberId(Long memberId);
}
