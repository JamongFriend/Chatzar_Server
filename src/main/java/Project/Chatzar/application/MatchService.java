package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.match.matchPreference.MatchPreference;
import Project.Chatzar.Domain.match.matchRequest.MatchRequest;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestStatus;
import Project.Chatzar.presentation.dto.match.response.MatchResult;
import Project.Chatzar.Domain.match.*;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.match.matchPreference.MatchPreferenceRepository;
import Project.Chatzar.Domain.match.MatchRepository;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        MatchCondition matchCondition = matchPreferenceRepository.findByMember(member)
                .map(MatchPreference::getCondition)
                .orElse(new MatchCondition(null, null, null, null));

        MatchRequest myRequest = new MatchRequest(member, matchCondition);
        matchRequestRepository.save(myRequest);

        return doMatch(myRequest);
    }

    @Transactional
    public void cancelMatch (Member member){
        MatchRequest matchRequest = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(member, MatchRequestStatus.WAITING)
                .orElseThrow(() -> new IllegalStateException("취소할 매칭 대기 요청이 없습니다."));

        matchRequest.markCancelled();
    }

    public MatchRequestStatus getMyMatchStatus(Member member) {
        return matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(member, MatchRequestStatus.WAITING)
                .map(MatchRequest::getStatus)
                .orElse(MatchRequestStatus.MATCHED);
    }

    // 매칭 시도
    @Transactional
    protected MatchResult doMatch(MatchRequest myRequest) {
        if (myRequest.getStatus() != MatchRequestStatus.WAITING) {
            return MatchResult.waiting();
        }
        // 나 자신이 아닌, WAITING 상태의 다른 요청 하나 찾기
        var optionalPartner = matchRequestRepository
                .findFirstByStatusAndRequesterNotOrderByCreatedAtAsc(
                        myRequest.getRequester(), MatchRequestStatus.WAITING
                );
        // 상대가 없으면 → 나는 계속 WAITING 상태로 남음
        if (optionalPartner.isEmpty()) {
            return MatchResult.waiting();
        }

        MatchRequest partnerRequest = optionalPartner.get();

        Member memberA = myRequest.getRequester();
        Member memberB = partnerRequest.getRequester();
        Match match = new Match(null, memberA, memberB);
        matchRepository.save(match);

        ChatRoom chatRoom = ChatRoom.create(memberA, memberB);
        chatRoomRepository.save(chatRoom);

        myRequest.markMatched();
        partnerRequest.markMatched();

        return MatchResult.matched(match.getId(), chatRoom.getId(), memberB);
    }

}
