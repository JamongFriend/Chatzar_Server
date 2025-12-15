package Project.Chatzar.Domain.chatRoom;

import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository {
    List<ChatRoom> findByMemberAOrMemberB(Member memberA, Member memberB);

    // 특정 매칭과 연결된 방 찾기
    ChatRoom findByMatchId(Long matchId);
}