package Project.Chatzar.repository;

import Project.Chatzar.Domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    List<Message> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long lastMessageId);
}