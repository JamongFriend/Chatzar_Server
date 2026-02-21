package Project.Chatzar.application;

import Project.Chatzar.Domain.friendship.Friendship;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
import Project.Chatzar.Domain.friendship.FriendshipStatus;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.presentation.dto.friendship.FriendshipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    public void sendFriendRequest(Member requester, Long targetId){
        Member targetMember = memberRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("대상을 찾을 수 없습니다."));

        if (friendshipRepository.existsByMemberIdAndFriendId(requester.getId(), targetId)) {
            throw new IllegalStateException("이미 친구이거나 대기 중인 요청이 있습니다.");
        }

        Friendship friendship = new Friendship(requester, targetMember, FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
    }

    public void acceptFriendRequest(Long friendshipId, Member currentMember) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다."));

        if (!friendship.getMemberB().getId().equals(currentMember.getId())) {
            throw new IllegalStateException("본인에게 온 요청만 수락할 수 있습니다.");
        }

        friendship.acceptRequset();

        chatRoomService.unlockRelatedChatRoom(friendship.getMemberA(), friendship.getMemberB());
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponse> getPendingRequests(Member member) {
        return friendshipRepository.findByMemberBAndStatus(member, FriendshipStatus.PENDING)
                .stream()
                .map(f -> new FriendshipResponse(
                        f.getId(),
                        f.getMemberA().getNickname(),
                        f.getStatus().name()
                ))
                .toList();
    }
}
