package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.application.MatchService;
import Project.Chatzar.presentation.dto.request.MatchConditionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matchPreference")
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
}
