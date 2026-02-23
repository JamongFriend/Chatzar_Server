package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomStatus;
import Project.Chatzar.Domain.chatRoom.ChatRoomType;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import Project.Chatzar.Domain.friendship.FriendshipStatus;
import Project.Chatzar.Domain.match.Match;
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
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public ChatRoom createRoom(Member memberA, Member memberB, Match match) {
        ChatRoom room = ChatRoom.create(memberA, memberB, match);
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

        if (!chatRoom.getMemberA().getId().equals(memberId) && !chatRoom.getMemberB().getId().equals(memberId)) {
            throw new IllegalStateException("해당 채팅방을 종료할 권한이 없습니다.");
        }

        boolean isFriend = friendshipRepository.existsByMemberAAndMemberBAndStatus(chatRoom.getMemberA(), chatRoom.getMemberB(), FriendshipStatus.ACCEPTED)
                || friendshipRepository.existsByMemberAAndMemberBAndStatus(chatRoom.getMemberB(), chatRoom.getMemberA(), FriendshipStatus.ACCEPTED);

        if (!isFriend) {
            chatRoom.lock();
        } else {
            chatRoom.close();
        }
    }

    @Transactional
    public void deleteRoom(Long roomId, Long memberId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다. id = " + roomId));

        if(!room.isParticipant(memberId)){
            throw new IllegalArgumentException("채팅방 참가자가 아닙니다.");
        }
        room.deleteRoom();
    }

    @Transactional
    public void unlockRelatedChatRoom(Member a, Member b) {
        chatRoomRepository.findLockRoomBetweenMembers(a, b)
                .ifPresent(room -> {room.unlock();});
    }
}
