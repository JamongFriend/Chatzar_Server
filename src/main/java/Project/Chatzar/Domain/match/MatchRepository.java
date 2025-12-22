package Project.Chatzar.Domain.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MatchRepository {
    List<Match> findByMemberAIdOrMemberBId(Long memberAId, Long memberBId);

    void save(Match match);
}
