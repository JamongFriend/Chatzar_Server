package Project.Chatzar.infrastructure.chatRoom;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
