package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.presentation.dto.friendship.FriendListResponse;
import Project.Chatzar.presentation.dto.friendship.FriendshipResponse;
import Project.Chatzar.testfixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class FriendshipServiceTest {
    @Autowired FriendshipService friendshipService;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("친구 요청을 수락하면 잠겨있던 랜덤 채팅방이 열리는 기능 테스트")
    void acceptFriendRequest_unlocksRoom() {
        Member memberA = memberRepository.save(MemberFixture.create("memberA@test.com", "A"));
        Member memberB = memberRepository.save(MemberFixture.create("memberB@test.com", "B"));

        friendshipService.sendFriendRequest(memberA, memberB.getId());

        List<FriendshipResponse> pendingRequests = friendshipService.getPendingRequests(memberB);
        Long friendshipId = pendingRequests.get(0).friendshipId();

        friendshipService.acceptFriendRequest(friendshipId, memberB);
    }

    @Test
    @DisplayName("존재하지 않는 요청 수락 시 예외 발생 테스트")
    void accept_NonExistentRequest_ThrowsException() {
        Member memberB = memberRepository.save(MemberFixture.create("memberB@test.com", "B"));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.acceptFriendRequest(-1L, memberB);
        });
    }

    @Test
    @DisplayName("본인에게 온 요청이 아닌데 수락 시도 시 예외 발생 테스트")
    void accept_NotMyRequest_ThrowsException() {
        Member memberA = memberRepository.save(MemberFixture.create("memberA@test.com", "A"));
        Member memberB = memberRepository.save(MemberFixture.create("memberB@test.com", "B"));
        Member memberC = memberRepository.save(MemberFixture.create("memberC@test.com", "C"));

        friendshipService.sendFriendRequest(memberA, memberB.getId());
        Long friendshipId = friendshipService.getPendingRequests(memberB).get(0).friendshipId();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            friendshipService.acceptFriendRequest(friendshipId, memberC);
        });
    }

    @Test
    @DisplayName("수락된 친구 목록만 정확하게 조회되는지 테스트")
    void getFriendList_Success() {
        Member me = memberRepository.save(MemberFixture.create("me@test.com", "나"));
        Member friendA = memberRepository.save(MemberFixture.create("friendA@test.com", "친구A"));
        Member friendB = memberRepository.save(MemberFixture.create("friendB@test.com", "친구B"));
        Member stranger = memberRepository.save(MemberFixture.create("stranger@test.com", "모르는사람"));

        friendshipService.sendFriendRequest(me, friendA.getId());
        Long id1 = friendshipService.getPendingRequests(friendA).get(0).friendshipId();
        friendshipService.acceptFriendRequest(id1, friendA);

        friendshipService.sendFriendRequest(friendB, me.getId());
        Long id2 = friendshipService.getPendingRequests(me).get(0).friendshipId();
        friendshipService.acceptFriendRequest(id2, me);

        List<FriendListResponse> friendList = friendshipService.getFriendList(me.getId());

        org.assertj.core.api.Assertions.assertThat(friendList).hasSize(2);

        org.assertj.core.api.Assertions.assertThat(friendList)
                .extracting(FriendListResponse::nickname)
                .containsExactlyInAnyOrder("친구A", "친구B")
                .doesNotContain("모르는사람");

        org.assertj.core.api.Assertions.assertThat(friendList)
                .extracting(FriendListResponse::friendId)
                .containsExactlyInAnyOrder(friendA.getId(), friendB.getId());
    }
}
