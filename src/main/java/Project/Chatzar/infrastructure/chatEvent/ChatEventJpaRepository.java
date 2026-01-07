package Project.Chatzar.infrastructure.chatEvent;

import Project.Chatzar.Domain.chatEvent.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventJpaRepository extends JpaRepository<ChatEvent, Long> {
}
