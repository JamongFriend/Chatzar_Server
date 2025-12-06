package Project.Chatzar.repository;

import Project.Chatzar.Domain.match.MatchPreference;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchPreferenceRepository extends JpaRepository<MatchPreference, Long> {

    Optional<MatchPreference> findByMember(Member member);

    boolean existsByMemberId(Long memberId);
}
