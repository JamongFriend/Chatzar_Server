package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.application.MemberService;
import Project.Chatzar.presentation.dto.member.JoinRequest;
import Project.Chatzar.presentation.dto.member.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@Valid @RequestBody JoinRequest request) {
        return ResponseEntity.ok(memberService.join(request));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.ok(response);
    }
}
