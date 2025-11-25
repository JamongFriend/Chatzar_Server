package Project.Chatzar.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Chat {
    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    private Member member;

    private LocalDateTime localDateTime;

    public void startChat() {

    }
}
