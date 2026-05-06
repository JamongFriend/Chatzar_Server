package Project.Chatzar.Domain.match.matchRequest;

import Project.Chatzar.Domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MatchRequestRepository {
    List<MatchRequest> findByStatus(MatchRequestStatus status);

    List<MatchRequest> findByStatusOrderByCreatedAtAsc(MatchRequestStatus status);

    Optional<MatchRequest> findFirstByRequesterNotAndStatusOrderByCreatedAtAsc(
            Member requester, MatchRequestStatus status
    );

    Optional<MatchRequest> findFirstByRequesterAndStatusOrderByCreatedAtDesc(Member requester, MatchRequestStatus status);

    MatchRequest save(MatchRequest myRequest);

    MatchRequest saveAndFlush(MatchRequest myRequest);
}
