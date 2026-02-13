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

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    // 방 생성용 팩토리 메서드
    public static ChatRoom create(Member memberA, Member memberB) {
        ChatRoom room = new ChatRoom();
        room.memberA = memberA;
        room.memberB = memberB;
        room.status = ChatRoomStatus.MATCHED;
        room.type = ChatRoomType.RANDOM;
        room.createdAt = LocalDateTime.now();
        return room;
    }

    // 방삭제
    public void deleteRoom() {
        this.status = ChatRoomStatus.DELETED;
    }

    // 채팅 종료 후 방 잠금 상태
    public void lock() {
        if(this.status == ChatRoomStatus.MATCHED && this.type == ChatRoomType.RANDOM) {
            this.status = ChatRoomStatus.LOCKED;
        }
    }

    // 친구 관계 성공 후 방 잠금 해제
    public void unlock() {
        if(this.status == ChatRoomStatus.LOCKED && this.type == ChatRoomType.FRIEND) {
            this.status = ChatRoomStatus.ACTIVE;
        }
    }

    public boolean isParticipant(Member member) {
        Long id = member.getId();
        return (memberA != null && memberA.getId().equals(id))
                || (memberB != null && memberB.getId().equals(id));
    }

    public Long getOtherMemberId(Long memberId) {
        if(this.memberA.getId().equals(memberId)){
            return this.memberB.getId();
        }
        return this.memberA.getId();
    }
}
