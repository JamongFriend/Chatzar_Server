package Project.Chatzar.Domain.match.matchRequest;

import Project.Chatzar.Domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MatchRequestRepository {
    // 현재 대기 중인 요청들을 가져오는 쿼리
    List<MatchRequest> findByStatus(MatchRequestStatus status);

    Optional<MatchRequest> findFirstByRequesterNotAndStatusOrderByCreatedAtAsc(
            Member requester, MatchRequestStatus status
    );

    Optional<MatchRequest> findFirstByRequesterAndStatusOrderByCreatedAtDesc(Member requester, MatchRequestStatus status);

    MatchRequest save(MatchRequest myRequest);

    MatchRequest saveAndFlush(MatchRequest myRequest);
}
