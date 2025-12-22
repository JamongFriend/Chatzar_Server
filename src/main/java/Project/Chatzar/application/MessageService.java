package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.message.Message;
import Project.Chatzar.Domain.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public Message sendMessage(Long roomId, Long senderId, String content) {
        if(content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용이 비어있습니다.");
        }
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다. roomId=" + roomId));

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다. senderId=" + senderId));

        if (!room.isParticipant(sender)) {
            throw new IllegalStateException("채팅방 참여자가 아닙니다.");
        }

        Message message = new Message(room, sender, content.trim());
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getRecentMessages(Long roomId) {
        return messageRepository.findTop30ByChatRoomIdOrderByIdAsc(roomId);
    }

    @Transactional(readOnly = true)
    public List<Message> getOlderMessages(Long roomId, Long lastMessageId) {
        List<Message> desc = messageRepository
                .findTop30ByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, lastMessageId);
        Collections.reverse(desc);
        return desc;
    }

}
