package Project.Chatzar.Domain.match.matchRequest;

import Project.Chatzar.Domain.match.MatchCondition;
import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class MatchRequest {
    @Id
    @GeneratedValue
    @Column(name = "match_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member requester;

    @Embedded
    private MatchCondition condition;

    @Enumerated(EnumType.STRING)
    private MatchRequestStatus status;

    private LocalDateTime createdAt;

    protected MatchRequest() {}

    public MatchRequest(Member requester, MatchCondition condition) {
        this.requester = requester;
        this.condition = condition;
        this.status = MatchRequestStatus.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    public void markMatched() {
        this.status = MatchRequestStatus.MATCHED;
    }

    public void markCancelled() {
        if (this.status != MatchRequestStatus.WAITING) {
            throw new IllegalStateException("WAITING 상태만 취소할 수 있습니다.");
        }
        this.status = MatchRequestStatus.CANCELLED;
    }
}
