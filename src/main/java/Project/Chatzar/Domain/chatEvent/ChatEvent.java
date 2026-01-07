package Project.Chatzar.Domain.chatEvent;
import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ChatEvent {
    @Id
    @GeneratedValue
    @Column(name = "chat_event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatEventType type;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected ChatEvent() {}

    private ChatEvent(ChatRoom chatRoom, Member member, ChatEventType type) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.type = type;
    }

    public static ChatEvent join(ChatRoom chatRoom, Member member) {
        return new ChatEvent(chatRoom, member, ChatEventType.JOIN);
    }

    public static ChatEvent leave(ChatRoom chatRoom, Member member) {
        return new ChatEvent(chatRoom, member, ChatEventType.LEAVE);
    }
}
