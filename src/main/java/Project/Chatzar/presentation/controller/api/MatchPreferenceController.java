package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.match.MatchCondition;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.application.MatchService;
import Project.Chatzar.presentation.dto.match.request.MatchConditionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match-preference")
public class MatchPreferenceController {
    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @PostMapping("/preference")
    public ResponseEntity<Void> updatePreference(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MatchConditionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));

        matchService.updatePreference(member, request.toCondition());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preference")
    public ResponseEntity<MatchConditionRequest> getPreference(
            @AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));

        MatchCondition condition = matchService.getPreference(member);
        return ResponseEntity.ok(MatchConditionRequest.fromCondition(condition));
    }
}
