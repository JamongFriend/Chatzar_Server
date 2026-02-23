package Project.Chatzar.infrastructure.chatRoom;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.chatRoom.ChatRoomStatus;
import Project.Chatzar.Domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final EntityManager em;

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

    @Override
    public List<ChatRoom> findAll() {
        return chatRoomJpaRepository.findAll();
    }

    @Override
    public Optional<ChatRoom> findLockRoomBetweenMembers(Member m1, Member m2) {
        String jpql = "SELECT cr FROM ChatRoom cr " +
                "WHERE ((cr.memberA = :m1 AND cr.memberB = :m2) OR " +
                "(cr.memberA = :m2 AND cr.memberB = :m1)) " +
                "AND cr.status = :status";
        try {
            ChatRoom result = em.createQuery(jpql, ChatRoom.class)
                    .setParameter("m1", m1)
                    .setParameter("m2", m2)
                    .setParameter("status", ChatRoomStatus.LOCKED)
                    .getSingleResult();
            return Optional.of(result);
        }catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long count() {
        return chatRoomJpaRepository.count();
    }
}
