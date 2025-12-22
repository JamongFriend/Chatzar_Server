package Project.Chatzar.Domain.chatRoom;

import Project.Chatzar.Domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    List<ChatRoom> findByMemberAOrMemberB(Member memberA, Member memberB);

    // 특정 매칭과 연결된 방 찾기
    ChatRoom findByMatchId(Long matchId);

    ChatRoom save(ChatRoom room);

    Optional<ChatRoom> findById(Long roomId);
}
