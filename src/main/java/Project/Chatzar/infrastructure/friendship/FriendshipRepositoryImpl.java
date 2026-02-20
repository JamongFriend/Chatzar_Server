package Project.Chatzar.infrastructure.friendship;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public boolean existsByMemberIdAndFriendId(Long memberId, Long partnerId) {
        return friendshipJpaRepository.existsByMemberIdAndFriendId(memberId, partnerId);
    }
}
