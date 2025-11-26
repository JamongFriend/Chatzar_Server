package Project.Chatzar.Domain;

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

    public void close() {
        this.status = ChatRoomStatus.CLOSED;
    }
}
