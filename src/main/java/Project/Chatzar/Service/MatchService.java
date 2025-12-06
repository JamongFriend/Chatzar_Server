package Project.Chatzar.Service;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.dto.MatchResult;
import Project.Chatzar.Domain.match.*;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.repository.ChatRoomRepository;
import Project.Chatzar.repository.MatchPreferenceRepository;
import Project.Chatzar.repository.MatchRepository;
import Project.Chatzar.repository.MatchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchPreferenceRepository matchPreferenceRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 매칭 선호 저장 및 수정
    @Transactional
    public void updatePreference(Member member, MatchCondition matchCondition) {
        matchPreferenceRepository.findByMember(member)
                .ifPresentOrElse(
                        existiongPref -> existiongPref.update(matchCondition),
                        () -> {
                            MatchPreference newPref = new MatchPreference(member, matchCondition);
                            matchPreferenceRepository.save(newPref);
                        });
    }

    // 매칭 요청 생성
    @Transactional
    public MatchResult requestMatch(Member member) {
        //매칭 조건 조회
        MatchCondition matchCondition = matchPreferenceRepository.findByMember(member)
                .map(MatchPreference::getCondition)
                .orElse(new MatchCondition(null, null, null, null));

        //매칭 요청 엔티티 생성
        MatchRequest myRequest = new MatchRequest(member, matchCondition);
        matchRequestRepository.save(myRequest);

        // 매칭 시도
        return doMatch(myRequest);
    }

    @Transactional
    protected MatchResult doMatch(MatchRequest myRequest) {
        if (myRequest.getStatus() != MatchRequestStatus.WAITING) {
            return MatchResult.waiting();
        }
        // 나 자신이 아닌, WAITING 상태의 다른 요청 하나 찾기
        var optionalPartner = matchRequestRepository
                .findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(
                        MatchRequestStatus.WAITING,
                        myRequest.getRequester()
                );
        // 상대가 없으면 → 나는 계속 WAITING 상태로 남음
        if (optionalPartner.isEmpty()) {
            return MatchResult.waiting();
        }

        MatchRequest partnerRequest = optionalPartner.get();

        Member me = myRequest.getRequester();
        Member partner = partnerRequest.getRequester();
        // Match 엔티티 생성 & 저장
        Match match = new Match(null, me, partner);
        matchRepository.save(match);

        // ChatRoom 엔티티 생성 & 저장
        ChatRoom chatRoom = new ChatRoom(me, partner); // 아래 ChatRoom 코드에 생성자 추가해줄 거야
        chatRoomRepository.save(chatRoom);

        // 두 요청 상태 MATCHED로 변경
        myRequest.markMatched();
        partnerRequest.markMatched();

        return MatchResult.matched(match.getId(), chatRoom.getId(), partner);
    }

}
