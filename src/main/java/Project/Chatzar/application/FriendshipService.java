package Project.Chatzar.application;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import Project.Chatzar.Domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    public void acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow();
        friendship.acceptRequset();

        unlockRelatedChatRoom(friendship.getRequester(), friendship.getReceiver());
    }


}
