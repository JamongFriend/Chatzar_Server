package Project.Chatzar.repository;

import Project.Chatzar.Domain.match.MatchPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchPreferenceRepository extends JpaRepository<MatchPreference, Long> {

    Optional<MatchPreference> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
