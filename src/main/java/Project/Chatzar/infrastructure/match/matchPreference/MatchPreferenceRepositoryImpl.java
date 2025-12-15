package Project.Chatzar.infrastructure.match.matchPreference;

import Project.Chatzar.Domain.match.matchPreference.MatchPreference;
import Project.Chatzar.Domain.match.matchPreference.MatchPreferenceRepository;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchPreferenceRepositoryImpl implements MatchPreferenceRepository {
    private final MatchPreferenceJpaRepository matchPreferenceJpaRepository;

    @Override
    public Optional<MatchPreference> findByMember(Member member) {
        return matchPreferenceJpaRepository.findByMember(member);
    }

    @Override
    public boolean existsByMemberId(Long memberId) {
        return matchPreferenceJpaRepository.existsByMemberId(memberId);
    }
}
