package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.match.matchPreference.MatchPreference;
import Project.Chatzar.Domain.match.matchRequest.MatchRequest;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestStatus;
import Project.Chatzar.presentation.dto.match.response.MatchResponse;
import Project.Chatzar.Domain.match.*;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import Project.Chatzar.Domain.match.matchPreference.MatchPreferenceRepository;
import Project.Chatzar.Domain.match.MatchRepository;
import Project.Chatzar.Domain.match.matchRequest.MatchRequestRepository;
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

    public MatchCondition getPreference(Member member) {
        return matchPreferenceRepository.findByMember(member)
                .map(MatchPreference::getCondition)
                .orElse(new MatchCondition(null, null, null, null, null));
    }

    // 매칭 요청 생성
    @Transactional
    public MatchResponse requestMatch(Member member) {
        MatchCondition matchCondition = matchPreferenceRepository.findByMember(member)
                .map(MatchPreference::getCondition)
                .orElse(new MatchCondition(null, null, null, null, null));

        MatchRequest myRequest = new MatchRequest(member, matchCondition);
        matchRequestRepository.saveAndFlush(myRequest);

        return MatchResponse.waiting();
    }

    @Transactional
    public MatchResponse tryMatching(Member member) {
        MatchRequest myRequest = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(member, MatchRequestStatus.WAITING)
                .orElseThrow(() -> new IllegalStateException("대기 중인 매칭 요청이 없습니다."));

        return doMatch(myRequest);
    }

    @Transactional
    public void cancelMatch (Member member){
        MatchRequest matchRequest = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(member, MatchRequestStatus.WAITING)
                .orElseThrow(() -> new IllegalStateException("취소할 매칭 대기 요청이 없습니다."));

        matchRequest.markCancelled();
    }

    @Transactional
    public MatchResponse getMatchStatus(Member member) {
        // 1. 대기 중인 요청이 있는지 확인
        Optional<MatchRequest> waitingRequest = matchRequestRepository
                .findFirstByRequesterAndStatusOrderByCreatedAtDesc(member, MatchRequestStatus.WAITING);

        if (waitingRequest.isPresent()) {
            return MatchResponse.waiting();
        }

        // 2. 대기 중인 요청이 없다면 최신 매칭 확인
        return matchRepository.findLatestMatch(member.getId())
                .map(match -> {
                    ChatRoom chatRoom = chatRoomRepository.findByMatchId(match.getId());
                    if (chatRoom == null) return MatchResponse.waiting();
                    Member partner = match.getOtherMember(member);
                    return MatchResponse.matched(match.getId(), chatRoom.getId(), partner);
                })
                .orElse(MatchResponse.waiting());
    }

    // 매칭 시도
    @Transactional
    protected MatchResponse doMatch(MatchRequest myRequest) {
        if (myRequest.getStatus() != MatchRequestStatus.WAITING) {
            return MatchResponse.waiting();
        }
        var optionalPartner = matchRequestRepository
                .findFirstByRequesterNotAndStatusOrderByCreatedAtAsc(
                        myRequest.getRequester(), MatchRequestStatus.WAITING
                );
        if (optionalPartner.isEmpty()) {
            return MatchResponse.waiting();
        }

        MatchRequest partnerRequest = optionalPartner.get();

        Member memberA = myRequest.getRequester();
        Member memberB = partnerRequest.getRequester();

        Match match = new Match(null, memberA, memberB);
        match = matchRepository.save(match);

        ChatRoom chatRoom = ChatRoom.create(memberA, memberB, match);
        chatRoom = chatRoomRepository.save(chatRoom);

        myRequest.markMatched();
        partnerRequest.markMatched();

        return MatchResponse.matched(match.getId(), chatRoom.getId(), memberB);
    }

}
