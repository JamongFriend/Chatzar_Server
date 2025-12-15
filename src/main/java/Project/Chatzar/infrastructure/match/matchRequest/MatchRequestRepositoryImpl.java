package Project.Chatzar.infrastructure.match.matchRequest;

import Project.Chatzar.Domain.match.matchRequest.MatchRequest;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestRepository;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestStatus;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchRequestRepositoryImpl implements MatchRequestRepository {
    private final MatchRequestJpaRepository matchRequestJpaRepository;

    @Override
    public List<MatchRequest> findByStatus(MatchRequestStatus status) {
        return matchRequestJpaRepository.findByStatus(status);
    }

    @Override
    public Optional<MatchRequest> findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(MatchRequestStatus status, Member requester) {
        return matchRequestJpaRepository.findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(status, requester);
    }

    @Override
    public void save(MatchRequest myRequest) {

    }
}
