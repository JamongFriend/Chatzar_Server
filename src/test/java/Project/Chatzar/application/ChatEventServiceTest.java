package Project.Chatzar.application;

import Project.Chatzar.Domain.chatEvent.ChatEventRepository;
import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.member.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatEventServiceTest {
    @Autowired ChatEventService chatEventService;
    @Autowired ChatEventRepository chatEventRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("채팅방 입장 로그가 정상적으로 저장되는지 테스트")
    void logJoin_success() {
        Member memberA = memberRepository.save(new Member("A", "a@test.com", "123", "nickA", "1234", 20L, MemberStatus.ACTIVE));
        Member memberB = memberRepository.save(new Member("B", "b@test.com", "123", "nickB", "2345", 20L, MemberStatus.ACTIVE));

        ChatRoom room = chatRoomRepository.save(ChatRoom.create(memberA, memberB, null));

        chatEventService.logJoin(room.getId(), memberA.getId());

        assertThat(chatEventRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 입장 로그를 남길 때 예외 발생")
    void logJoin_fail_memberNotFound() {
        Member memberA = memberRepository.save(new Member("A", "a@test.com", "123", "nickA", "1234", 20L, MemberStatus.ACTIVE));
        Member memberB = memberRepository.save(new Member("B", "b@test.com", "123", "nickB", "2345", 20L, MemberStatus.ACTIVE));

        ChatRoom room = chatRoomRepository.save(ChatRoom.create(memberA, memberB, null));

        Long invalidMemberId = 9999L;

        assertThatThrownBy(() -> chatEventService.logJoin(room.getId(), invalidMemberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 ID로 입장 로그를 남길 때 예외 발생")
    void logJoin_fail_roomNotFound() {
        // Given: 회원만 생성
        Member member = memberRepository.save(new Member("테스터", "test@test.com", "123", "닉네임", "1234", 25L, MemberStatus.ACTIVE));

        // 존재하지 않을 법한 채팅방 ID 설정
        Long invalidRoomId = 9999L;

        // When & Then
        assertThatThrownBy(() -> chatEventService.logJoin(invalidRoomId, member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("채팅방이 없습니다.");
    }
}
