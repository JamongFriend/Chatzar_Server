package Project.Chatzar.infrastructure.auth;

import Project.Chatzar.Domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findTopByMemberIdAndRevokedFalseOrderByIdDesc(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    RefreshToken save(RefreshToken token);

    void deleteByMemberId(Long memberId);
}
