package Project.Chatzar.Domain.match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Optional<Match> findExactMatch(Long memberAId, Long memberBId);

    Match save(Match match);
}
