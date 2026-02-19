package Project.Chatzar.Domain.friendship;

import java.util.Optional;

public interface FriendshipRepository {
    Optional<Friendship> findById(Long friendshipId);
}
