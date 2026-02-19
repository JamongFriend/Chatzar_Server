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
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friendship(Long id, Member requester, Member receiver, FriendshipStatus status) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public void acceptRequset() {
        if(this.status == FriendshipStatus.PENDING) {
            this.status = FriendshipStatus.ACCEPTED;
        }
    }
}
