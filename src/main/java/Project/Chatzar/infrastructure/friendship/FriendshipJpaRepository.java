package Project.Chatzar.infrastructure.friendship;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipStatus;
import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipJpaRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE " +
            "(f.memberA.id = :mId AND f.memberB.id = :pId) OR " +
            "(f.memberA.id = :pId AND f.memberB.id = :mId)")
    boolean existsByMemberIdAndFriendId(@Param("mId") Long memberId, @Param("pId") Long targetId);

    @Query("SELECT f FROM Friendship f " +
            "JOIN FETCH f.memberA " +
            "JOIN FETCH f.memberB " +
            "WHERE (f.memberA.id = :memberId OR f.memberB.id = :memberId) " +
            "AND f.status = 'ACCEPTED'")
    List<Friendship> findAllFriends(@Param("memberId") Long memberId);

    Friendship save(Friendship friendship);

    List<Friendship> findByMemberBAndStatus(Member memberB, FriendshipStatus status);

    boolean existsByMemberAAndMemberBAndStatus(Member memberA, Member memberB, FriendshipStatus status);
}
