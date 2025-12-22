package Project.Chatzar.Domain.message;

import java.util.List;

public interface MessageRepository{
    List<Message> findTop30ByChatRoomIdOrderByIdAsc(Long chatRoomId);

    List<Message> findTop30ByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long lastMessageId);

    Message save(Message message);
}
