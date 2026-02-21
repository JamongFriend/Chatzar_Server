package Project.Chatzar.infrastructure.friendship;

import Project.Chatzar.Domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipJpaRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE " +
            "(f.memberA.id = :mId AND f.memberB.id = :pId) OR " +
            "(f.memberA.id = :pId AND f.memberB.id = :mId)")
    boolean existsByMemberIdAndFriendId(@Param("mId") Long memberId, @Param("pId") Long partnerId);
}
