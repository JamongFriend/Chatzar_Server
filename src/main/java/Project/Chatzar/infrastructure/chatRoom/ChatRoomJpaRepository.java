package Project.Chatzar.infrastructure.chatRoom;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByMemberAOrMemberB(Member memberA, Member memberB);

    // 특정 매칭과 연결된 방 찾기
    ChatRoom findByMatchId(Long matchId);

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE ((cr.userA = :m1 AND cr.userB = :m2) OR (cr.userA = :m2 AND cr.userB = :m1)) " +
            "AND cr.status = 'LOCKED'")
    Optional<ChatRoom> findLockedRoomBetween(@Param("m1") Member m1, @Param("m2") Member m2);
}
