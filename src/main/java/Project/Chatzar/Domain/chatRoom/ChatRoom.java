package Project.Chatzar.Domain.chatRoom;

import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_a_id", nullable = false)
    private Member memberA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_b_id", nullable = false)
    private Member memberB;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

    // 방 생성용 팩토리 메서드
    public static ChatRoom create(Member memberA, Member memberB) {
        ChatRoom room = new ChatRoom();
        room.memberA = memberA;
        room.memberB = memberB;
        room.status = ChatRoomStatus.ACTIVE;
        room.createdAt = LocalDateTime.now();
        return room;
    }

    public void close() {
        this.status = ChatRoomStatus.CLOSED;
    }

    public boolean isParticipant(Member member) {
        Long id = member.getId();
        return (memberA != null && memberA.getId().equals(id))
                || (memberB != null && memberB.getId().equals(id));
    }
}
