package Project.Chatzar.repository;

import Project.Chatzar.Domain.match.MatchRequest;
import Project.Chatzar.Domain.match.MatchRequestStatus;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    // 현재 대기 중인 요청들을 가져오는 쿼리
    List<MatchRequest> findByStatus(MatchRequestStatus status);

    Optional<MatchRequest> findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(
            MatchRequestStatus status,
            Member requester
    );
}