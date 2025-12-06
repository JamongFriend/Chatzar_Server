package Project.Chatzar.Domain.match;

import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class MatchPreference {

    @Id
    @GeneratedValue
    @Column(name = "match_preference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Embedded
    private MatchCondition condition;

    private LocalDateTime updatedAt;

    protected MatchPreference() {}

    public MatchPreference(Member member, MatchCondition condition) {
        this.member = member;
        this.condition = condition;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(MatchCondition newCondition) {
        this.condition = newCondition;
        this.updatedAt = LocalDateTime.now();
    }
}
