package Project.Chatzar.infrastructure.match.matchRequest;

import Project.Chatzar.Domain.match.matchRequest.MatchRequest;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestStatus;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRequestJpaRepository  extends JpaRepository<MatchRequest, Long> {

    // 현재 대기 중인 요청들을 가져오는 쿼리
    List<MatchRequest> findByStatus(MatchRequestStatus status);

    Optional<MatchRequest> findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(
            Member requester, MatchRequestStatus status
    );

    Optional<MatchRequest> findFirstByRequesterAndStatusOrderByCreatedAtDesc(Member requester, MatchRequestStatus status);
}
