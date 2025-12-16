package Project.Chatzar.presentation.controller;

import Project.Chatzar.application.MemberService;
import Project.Chatzar.presentation.dto.LoginRequest;
import Project.Chatzar.presentation.dto.MemberResponse;
import Project.Chatzar.presentation.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegisterRequest request) {
        Long memberId = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody LoginRequest request) {
        MemberResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.ok(response);
    }
}
