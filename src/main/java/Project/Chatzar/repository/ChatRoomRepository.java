package Project.Chatzar.repository;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByMemberAOrMemberB(Member memberA, Member memberB);

    // 특정 매칭과 연결된 방 찾기 (옵션)
    ChatRoom findByMatchId(Long matchId);
}