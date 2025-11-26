package Project.Chatzar.repository;

import Project.Chatzar.Domain.MatchRequest;
import Project.Chatzar.Domain.MatchRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    // 현재 대기 중인 요청들을 가져오는 쿼리
    List<MatchRequest> findByStatus(MatchRequestStatus status);

    // 특정 유저의 WAITING 요청이 있는지
    boolean existsByRequesterIdAndStatus(Long memberId, MatchRequestStatus status);
}