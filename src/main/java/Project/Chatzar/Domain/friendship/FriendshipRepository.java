package Project.Chatzar.Domain.friendship;

import Project.Chatzar.Domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository {
    Optional<Friendship> findById(Long friendshipId);

    List<Friendship> findAllFriends(Long memberId);

    boolean existsByMemberIdAndFriendId(Long memberId, Long targetId);

    Friendship save(Friendship friendship);

    List<Friendship> findByMemberBAndStatus(Member memberB, FriendshipStatus status);

    boolean existsByMemberAAndMemberBAndStatus(Member memberA, Member memberB, FriendshipStatus status);
}
