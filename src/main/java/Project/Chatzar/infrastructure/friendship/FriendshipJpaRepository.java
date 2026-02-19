package Project.Chatzar.infrastructure.friendship;

import Project.Chatzar.Domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipJpaRepository extends JpaRepository<Friendship, Long> {
}
