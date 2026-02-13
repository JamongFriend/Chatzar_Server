package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomType;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createRoom(Member memberA, Member memberB) {
        ChatRoom room = ChatRoom.create(memberA, memberB);
        return chatRoomRepository.save(room);
    }

    public ChatRoom getRoom(Long roomId){
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다. id = " + roomId));

    }

    public List<ChatRoom> getMyRooms(Member member) {
        return chatRoomRepository.findByMemberAOrMemberB(member, member);
    }

    @Transactional
    public void closeRandomChatRoom(Long roomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다. id = " + roomId));

        if(chatRoom.getType() == ChatRoomType.RANDOM) {
            Long partnerId = chatRoom.getOtherMemberId(memberId);
            // TODO: 친구 추가 기능 만든 후 서로 친구인지 확인
            boolean isFriend;

            if(!isFriend) {
                chatRoom.lock();
            }else {
                chatRoom.unlock();
            }

        }
    }

    @Transactional
    public void deleteRoom(Long roomId, Member member) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다. id = " + roomId));

        if(!room.isParticipant(member)){
            throw new IllegalArgumentException("채팅방 참가자가 아닙니다.");
        }
        room.deleteRoom();
    }
}
