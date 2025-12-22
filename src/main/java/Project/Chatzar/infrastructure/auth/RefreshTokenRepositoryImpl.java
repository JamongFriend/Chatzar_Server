package Project.Chatzar.infrastructure.auth;

import Project.Chatzar.Domain.auth.RefreshToken;
import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findValidByMemberId(Long memberId) {
        return refreshTokenJpaRepository.findTopByMemberIdAndRevokedFalseOrderByIdDesc(memberId)
                .filter(rt -> rt.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        return refreshTokenJpaRepository.save(token);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        refreshTokenJpaRepository.deleteByMemberId(memberId);
    }
}
