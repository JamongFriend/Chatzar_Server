package Project.Chatzar.application;

import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.auth.request.LoginRequest;
import Project.Chatzar.presentation.dto.member.JoinRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired private AuthService authService;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("로그인 시 비밀번호가 틀리면 예외가 발생하는지 테스트")
    void login_wrongPassword() {
        memberService.join(new JoinRequest("테스터", "wrong_pw_test@test.com", "password123", "닉네임", 20L));

        assertThatThrownBy(() -> authService.login(new LoginRequest("wrong_pw_test@test.com", "wrong_password")))
                .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("정상적으로 로그아웃이 되는지 테스트")
    void logout_success() {
        String email = "logout@test.com";
        String rawPassword = "password123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Member member = new Member(
                "테스트유저",
                email,
                encodedPassword,
                "테스트닉네임",
                "1234",
                27L,
                MemberStatus.ACTIVE
        );
        memberRepository.save(member);

        em.flush();
        em.clear();

        LoginRequest loginRequest = new LoginRequest(email, rawPassword);
        authService.login(loginRequest);
        authService.logout(member.getId());

        em.flush();
        em.clear();

        boolean exists = refreshTokenRepository.findValidByMemberId(member.getId()).isPresent();
        assertThat(exists).isFalse();
    }
}
