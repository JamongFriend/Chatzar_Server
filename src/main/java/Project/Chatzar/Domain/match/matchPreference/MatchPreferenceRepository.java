package Project.Chatzar.Domain.match.matchPreference;

import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchPreferenceRepository {

    Optional<MatchPreference> findByMember(Member member);

    boolean existsByMemberId(Long memberId);
}
