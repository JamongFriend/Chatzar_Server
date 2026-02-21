package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.chatRoom.ChatRoomStatus;
import Project.Chatzar.Domain.friendship.FriendshipRepository;
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

@SpringBootTest
@Transactional
class ChatRoomServiceTest {
    @Autowired ChatRoomService chatRoomService;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MatchRepository matchRepository;

    @Test
    @DisplayName("상대방과 친구가 아닐 때 방을 닫으면 LOCKED 상태가 되어야 한다")
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


}
