package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.application.MatchService;
import Project.Chatzar.presentation.dto.match.MatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {
    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @PostMapping("/request")
    public ResponseEntity<MatchResult> requestMatch(@AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        MatchResult matchResult = matchService.requestMatch(member);
        return ResponseEntity.ok(matchResult);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelMatch(@AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        matchService.cancelMatch(member);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<String> status(@AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        return ResponseEntity.ok(matchService.getMyMatchStatus(member).name());
    }

}
