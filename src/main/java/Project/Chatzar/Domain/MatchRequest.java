package Project.Chatzar.Domain;

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

    public void cancel() {
        this.status = MatchRequestStatus.CANCELED;
    }
}
