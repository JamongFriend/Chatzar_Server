package Project.Chatzar.infrastructure.match;

import Project.Chatzar.Domain.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchJpaRepository  extends JpaRepository<Match, Long> {
    List<Match> findByMemberAIdOrMemberBId(Long memberAId, Long memberBId);
}
