package Project.Chatzar.repository;

import Project.Chatzar.Domain.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByMemberAIdOrMemberBId(Long memberAId, Long memberBId);
}
