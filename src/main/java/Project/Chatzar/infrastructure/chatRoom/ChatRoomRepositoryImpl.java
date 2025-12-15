package Project.Chatzar.infrastructure.chatRoom;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;


    @Override
    public List<ChatRoom> findByMemberAOrMemberB(Member memberA, Member memberB) {
        return chatRoomJpaRepository.findByMemberAOrMemberB(memberA, memberB);
    }

    @Override
    public ChatRoom findByMatchId(Long matchId) {
        return chatRoomJpaRepository.findByMatchId(matchId);
    }

    @Override
    public ChatRoom save(ChatRoom room) {
        return chatRoomJpaRepository.save(room);
    }

    @Override
    public Optional<ChatRoom> findById(Long roomId) {
        return chatRoomJpaRepository.findById(roomId);
    }
}
