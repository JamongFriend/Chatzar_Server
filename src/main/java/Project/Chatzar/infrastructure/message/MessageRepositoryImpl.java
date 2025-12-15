package Project.Chatzar.infrastructure.message;

import Project.Chatzar.Domain.message.Message;
import Project.Chatzar.Domain.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final MessageJpaRepository messageJpaRepository;

    @Override
    public List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId) {
        return messageJpaRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }

    @Override
    public List<Message> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long lastMessageId) {
        return messageJpaRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(chatRoomId, lastMessageId);
    }
}
