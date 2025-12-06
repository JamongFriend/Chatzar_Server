package Project.Chatzar.Domain.match;

import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Match {
    @Id
    @GeneratedValue
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_a_id",nullable = false)
    private Member memberA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_b_id", nullable = false)
    private Member memberB;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    private LocalDateTime createdAt;
    private LocalDateTime endedAt;

    protected Match(){}

    public Match(Long id, Member memberA, Member memberB) {
        this.id = id;
        this.memberA = memberA;
        this.memberB = memberB;
        this.matchStatus = MatchStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void end() {
        this.matchStatus = MatchStatus.ENDED;
        this.endedAt = LocalDateTime.now();
    }

    public Member getOtherMember(Member me) {
        if (me.getId().equals(memberA.getId())) {
            return memberB;
        }
        if (me.getId().equals(memberB.getId())) {
            return memberA;
        }
        throw new IllegalArgumentException("매치에 속하지 않은 유저입니다.");
    }
}
