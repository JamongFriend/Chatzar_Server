package Project.Chatzar.application;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final ChatRoomService chatRoomService;

    public void acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow();
        friendship.acceptRequset();

        chatRoomService.unlockRelatedChatRoom(friendship.getRequester(), friendship.getReceiver());
    }
}
