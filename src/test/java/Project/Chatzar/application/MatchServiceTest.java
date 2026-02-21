package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.match.MatchCondition;
import Project.Chatzar.Domain.match.matchRequest.MatchRequest;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestRepository;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestStatus;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.presentation.dto.match.response.MatchResult;
import Project.Chatzar.testfixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MatchServiceTest {
    @Autowired MatchService matchService;
    @Autowired MemberRepository memberRepository;
    @Autowired MatchRequestRepository matchRequestRepository;
    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("대기 중인 상대가 있을 때 매칭을 요청하면 MATCHED 상태가 되고 채팅방이 생성되어야 한다")
    void matching_success_test() {
        Member partner = MemberFixture.create("partner@test.com", "상대방");
        memberRepository.save(partner);

        MatchRequest partnerRequest = new MatchRequest(partner, new MatchCondition(null, null, null, null));
        matchRequestRepository.save(partnerRequest);

        Member me = MemberFixture.create("me@test.com", "나");
        memberRepository.save(me);
        MatchResult result = matchService.requestMatch(me);

        // Then
        assertThat(result.isMatched()).isTrue();
        assertThat(result.getPartnerNickname()).isEqualTo("상대방");

        List<ChatRoom> rooms = chatRoomRepository.findAll();
        assertThat(rooms).hasSize(1);
    }

    @Test
    @DisplayName("대기 중인 상대가 없으면 WAITING 상태가 되고 채팅방이 생성되지 않아야 한다")
    void matching_waiting_test() {
        // Given
        Member me = MemberFixture.create("me@test.com", "나");
        memberRepository.save(me);

        // When
        MatchResult result = matchService.requestMatch(me);

        // Then
        assertThat(result.isMatched()).isFalse();
        MatchRequest request = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(me, MatchRequestStatus.WAITING)
                .orElseThrow();
        assertThat(request.getStatus()).isEqualTo(MatchRequestStatus.WAITING);
        assertThat(chatRoomRepository.count()).isEqualTo(0);

    }

    @Test
    @DisplayName("매칭 대기 중 취소를 요청하면 요청 상태가 CANCELLED로 변경되어야 한다")
    void cancel_match_test() {
        // Given
        Member me = MemberFixture.create("me@test.com", "나");
        memberRepository.save(me);
        matchService.requestMatch(me);

        // When
        matchService.cancelMatch(me);

        // Then
        var request = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(me, MatchRequestStatus.WAITING);
        assertThat(request.isEmpty()).isTrue();
    }
}
