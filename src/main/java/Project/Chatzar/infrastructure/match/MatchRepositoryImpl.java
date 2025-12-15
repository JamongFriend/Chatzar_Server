package Project.Chatzar.infrastructure.match;

import Project.Chatzar.Domain.match.Match;
import Project.Chatzar.Domain.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {
    private final MatchJpaRepository matchJpaRepository;


    @Override
    public List<Match> findByMemberAIdOrMemberBId(Long memberAId, Long memberBId) {
        return matchJpaRepository.findByMemberAIdOrMemberBId(memberAId, memberBId);
    }
}
