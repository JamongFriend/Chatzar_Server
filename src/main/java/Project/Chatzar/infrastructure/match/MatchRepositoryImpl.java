package Project.Chatzar.infrastructure.match;

import Project.Chatzar.Domain.match.Match;
import Project.Chatzar.Domain.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {
    private final MatchJpaRepository matchJpaRepository;


    @Override
    public Optional<Match> findExactMatch(Long memberAId, Long memberBId) {
        return matchJpaRepository.findExactMatch(memberAId, memberBId);
    }

    @Override
    public void save(Match match) {

    }
}
