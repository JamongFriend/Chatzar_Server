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
    public List<Message> findTop30ByChatRoomIdOrderByIdAsc(Long chatRoomId) {
        return messageJpaRepository.findTop30ByChatRoomIdOrderByIdAsc(chatRoomId);
    }

    @Override
    public List<Message> findTop30ByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long lastMessageId) {
        return messageJpaRepository.findTop30ByChatRoomIdAndIdLessThanOrderByIdDesc(chatRoomId, lastMessageId);
    }

    @Override
    public Message save(Message message) {
        return messageJpaRepository.save(message);
    }
}
