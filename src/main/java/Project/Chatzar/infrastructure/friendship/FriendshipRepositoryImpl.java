package Project.Chatzar.infrastructure.friendship;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import Project.Chatzar.Domain.friendship.FriendshipStatus;
import Project.Chatzar.Domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final FriendshipJpaRepository friendshipJpaRepository;

    @Override
    public Optional<Friendship> findById(Long friendshipId) {
        return friendshipJpaRepository.findById(friendshipId);
    }

    @Override
    public List<Friendship> findAllFriends(Long memberId) {
        return  friendshipJpaRepository.findAllFriends(memberId);
    }

    @Override
    public boolean existsByMemberIdAndFriendId(Long memberId, Long targetId) {
        return friendshipJpaRepository.existsByMemberIdAndFriendId(memberId, targetId);
    }

    @Override
    public Friendship save(Friendship friendship) {
        return friendshipJpaRepository.save(friendship);
    }

    @Override
    public List<Friendship> findByMemberBAndStatus(Member memberB, FriendshipStatus status) {
        return friendshipJpaRepository.findByMemberBAndStatus(memberB, status);
    }

    @Override
    public boolean existsByMemberAAndMemberBAndStatus(Member memberA, Member memberB, FriendshipStatus status) {
        return friendshipJpaRepository.existsByMemberAAndMemberBAndStatus(memberA, memberB, status);
    }

}
