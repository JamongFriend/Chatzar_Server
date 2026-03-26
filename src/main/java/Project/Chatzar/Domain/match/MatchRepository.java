package Project.Chatzar.Domain.match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Optional<Match> findExactMatch(Long memberAId, Long memberBId);

    Optional<Match> findLatestMatch(Long memberId);

    Match save(Match match);
}
