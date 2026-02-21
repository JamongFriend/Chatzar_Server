package Project.Chatzar.infrastructure.chatEvent;

import Project.Chatzar.Domain.chatEvent.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatEventJpaRepository extends JpaRepository<ChatEvent, Long> {
}
