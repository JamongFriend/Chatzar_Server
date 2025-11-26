package Project.Chatzar.repository;

import Project.Chatzar.Domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 내가 참여한 방들 조회
    List<ChatRoom> findByMemberAIdOrMemberBId(Long memberId1, Long memberId2);

    // 특정 매칭과 연결된 방 찾기 (옵션)
    ChatRoom findByMatchId(Long matchId);
}