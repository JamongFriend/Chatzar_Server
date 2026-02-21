package Project.Chatzar.infrastructure.match;

import Project.Chatzar.Domain.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchJpaRepository  extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE " +
            "(m.memberA.id = :memberAId AND m.memberB.id = :memberBId) OR " +
            "(m.memberA.id = :memberBId AND m.memberB.id = :memberAId)")
    Optional<Match> findExactMatch(@Param("memberAId") Long memberAId, @Param("memberBId") Long memberBId);

}
