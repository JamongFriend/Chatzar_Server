package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.testfixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FriendshipServiceTest {
    @Autowired FriendshipService friendshipService;
    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("친구 요청을 수락하면 잠겨있던 랜덤 채팅방이 활성화되어야 한다")
    void acceptFriendRequest_unlocksRoom() {
        Member memberA = MemberFixture.create("memberA@test.com", "A");
        Member memberB = MemberFixture.create("memberB@test.com", "B");

        friendshipService.sendFriendRequest(memberA, memberB.getId());

        friendshipService.acceptFriendRequest(, memberB);
    }

    @Test
    @DisplayName("존재하지 않는 요청 수락 시 예외 발생 테스트")
    void a() {

    }

    @Test
    @DisplayName("이미 수락된 요청 재수락 테스트")
    void b() {

    }

}
