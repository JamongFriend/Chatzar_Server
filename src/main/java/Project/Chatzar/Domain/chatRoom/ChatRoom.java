package Project.Chatzar.Domain.chatRoom;

import Project.Chatzar.Domain.match.Match;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    // 방 생성용 팩토리 메서드
    public static ChatRoom create(Member memberA, Member memberB, Match match) {
        ChatRoom room = new ChatRoom();
        room.memberA = memberA;
        room.memberB = memberB;
        room.match = match;
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

    public boolean isParticipant(Long memberId) {
        return (memberA != null && memberA.getId().equals(memberId))
                || (memberB != null && memberB.getId().equals(memberId));
    }

    public Long getOtherMemberId(Long memberId) {
        if(this.memberA == null || this.memberB == null){
            return null;
        }
        return this.memberA.getId().equals(memberId) ? this.memberB.getId() : this.memberA.getId();
    }
}
