package Project.Chatzar.presentation.controller;

import Project.Chatzar.application.AuthService;
import Project.Chatzar.presentation.dto.LoginRequest;
import Project.Chatzar.presentation.dto.MemberResponse;
import Project.Chatzar.presentation.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegisterRequest request) {
        Long memberId = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody LoginRequest request) {
        MemberResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
