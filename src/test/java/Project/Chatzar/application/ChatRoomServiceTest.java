package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.chatRoom.ChatRoomStatus;
import Project.Chatzar.Domain.match.Match;
import Project.Chatzar.Domain.match.MatchRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.testfixture.MatchFixture;
import Project.Chatzar.testfixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChatRoomServiceTest {
    @Autowired ChatRoomService chatRoomService;
    @Autowired FriendshipService friendshipService;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MatchRepository matchRepository;

    @Test
    @DisplayName("상대방과 친구가 아닐 때 방을 닫으면 LOCKED 상태가 되는지 테스트")
    void closeRandomChatRoom_LockTest() {
        //given
        Member me = MemberFixture.create("me@test.com", "나");
        Member partner = MemberFixture.create("partner@test.com", "상대방");
        memberRepository.save(me);
        memberRepository.save(partner);
        Match match = MatchFixture.create(me, partner);
        matchRepository.save(match);

        ChatRoom room = ChatRoom.create(me, partner, match);
        chatRoomRepository.save(room);
        chatRoomService.closeRandomChatRoom(room.getId(), me.getId());

        ChatRoom updatedRoom = chatRoomRepository.findById(room.getId())
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));

        assertThat(updatedRoom.getStatus()).isEqualTo(ChatRoomStatus.LOCKED);
    }
    @Test
    @DisplayName("방 참여자가 아닌 유저가 방을 닫으려 할 때 예외 발생 테스트")
    void closeRoom_NotParticipant_ThrowsException() {
        // given
        Member me = memberRepository.save(MemberFixture.create("me@test.com", "나"));
        Member partner = memberRepository.save(MemberFixture.create("partner@test.com", "상대"));
        Member hacker = memberRepository.save(MemberFixture.create("hacker@test.com", "해커"));

        Match match = matchRepository.save(MatchFixture.create(me, partner));
        ChatRoom room = chatRoomRepository.save(ChatRoom.create(me, partner, match));

        // when & then
        assertThatThrownBy(() -> chatRoomService.closeRandomChatRoom(room.getId(), hacker.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("이미 친구 관계인 상대와의 방을 닫으면 LOCKED가 안되는지 테스트")
    void closeRoom_AlreadyFriends_StayUnlocked() {
        Member me = memberRepository.save(MemberFixture.create("me@test.com", "나"));
        Member partner = memberRepository.save(MemberFixture.create("partner@test.com", "상대"));

        friendshipService.sendFriendRequest(me, partner.getId());
        Long fId = friendshipService.getPendingRequests(partner).get(0).friendshipId();
        friendshipService.acceptFriendRequest(fId, partner);

        Match match = matchRepository.save(MatchFixture.create(me, partner));
        ChatRoom room = chatRoomRepository.save(ChatRoom.create(me, partner, match));

        chatRoomService.closeRandomChatRoom(room.getId(), me.getId());

        ChatRoom updatedRoom = chatRoomRepository.findById(room.getId()).orElseThrow();
        assertThat(updatedRoom.getStatus()).isNotEqualTo(ChatRoomStatus.LOCKED);
    }

}
