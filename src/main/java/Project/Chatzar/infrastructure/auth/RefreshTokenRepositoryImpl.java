package Project.Chatzar.infrastructure.auth;

import Project.Chatzar.Domain.auth.RefreshToken;
import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByMemberId(Long memberId) {
        return refreshTokenJpaRepository.findByMemberId(memberId);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public void delete(RefreshToken refreshToken) {

    }
}
