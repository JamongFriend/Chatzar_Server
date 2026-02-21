package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomType;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
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

        if(chatRoom.getType() == ChatRoomType.RANDOM) {
            Long partnerId = chatRoom.getOtherMemberId(memberId);
            boolean isFriend = friendshipRepository.existsByMemberIdAndFriendId(memberId, partnerId);

            if(partnerId == null){
                this.deleteRoom(roomId, memberId);
                return;
            }

            if(!isFriend) {
                chatRoom.lock();
            }else {
                chatRoom.unlock();
            }
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
