package Project.Chatzar.Domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MessageRepository{
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    List<Message> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long lastMessageId);
}
