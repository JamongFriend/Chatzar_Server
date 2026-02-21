package Project.Chatzar.Domain.friendship;

import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_a_id")
    private Member memberA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_b_id")
    private Member memberB;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friendship(Long id, Member memberA, Member memberB, FriendshipStatus status) {
        this.id = id;
        this.memberA = memberA;
        this.memberB = memberB;
        this.status = status;
    }

    public void acceptRequset() {
        if(this.status == FriendshipStatus.PENDING) {
            this.status = FriendshipStatus.ACCEPTED;
        }
    }

    protected Friendship() {}
}
