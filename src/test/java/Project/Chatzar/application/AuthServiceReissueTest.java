package Project.Chatzar.application;

import Project.Chatzar.Domain.auth.JwtTokenProvider;
import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.auth.request.LoginRequest;
import Project.Chatzar.presentation.dto.auth.request.ReissueRequest;
import Project.Chatzar.presentation.dto.auth.request.SignUpRequest;
import Project.Chatzar.presentation.dto.auth.response.TokenResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceReissueTest {
    @Autowired private AuthService authService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;

    private String validRefreshToken;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(
                "이름", "reissue@test.com", passwordEncoder.encode("1234"),
                "닉네임", 27L, MemberStatus.ACTIVE));
        em.flush();
        em.clear();
        TokenResponse res = authService.login(new LoginRequest("reissue@test.com", "1234"));
        validRefreshToken = res.refreshToken();
    }

    @Test
    @DisplayName("유효한 토큰으로 재발급 시 기존 토큰은 무효화되고 새 토큰이 발급되는지 테스트")
    void reissue_success() throws InterruptedException {
        Thread.sleep(1000);
        TokenResponse result = authService.reissue(new ReissueRequest(validRefreshToken));

        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotEqualTo(validRefreshToken);
    }

    @Test
    @DisplayName("이미 사용되었거나 폐기(Revoke)된 토큰으로 재발급 시도 시 예외가 발생하는지 테스트")
    void reissue_fail_already_used() throws InterruptedException {
        String uniqueEmail = "fail_test_" + System.currentTimeMillis() + "@test.com";
        Member member = new Member("테스터", uniqueEmail, passwordEncoder.encode("1234"), "테스터닉", 20L, MemberStatus.ACTIVE);
        memberRepository.save(member);
        em.flush();
        em.clear();

        TokenResponse loginRes = authService.login(new LoginRequest(uniqueEmail, "1234"));
        String firstRefreshToken = loginRes.refreshToken();
        Thread.sleep(1000);
        authService.reissue(new ReissueRequest(firstRefreshToken));

        em.flush();
        em.clear();

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(firstRefreshToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RefreshToken이 일치하지 않습니다.");
    }
}
