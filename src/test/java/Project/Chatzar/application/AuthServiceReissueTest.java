package Project.Chatzar.application;

import Project.Chatzar.Domain.auth.RefreshToken;
import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.auth.request.LoginRequest;
import Project.Chatzar.presentation.dto.auth.request.ReissueRequest;
import Project.Chatzar.presentation.dto.auth.response.TokenResponse;
import Project.Chatzar.testfixture.MemberFixture;
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
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;
    @Autowired private RefreshTokenRepository refreshTokenRepository;

    private String validRefreshToken;
    private final String testEmail = "reissue@test.com";
    private final String testPassword = "password123!";

    @BeforeEach
    void setUp() {
        memberRepository.save(MemberFixture.create("reissue@test.com", "닉네임"));
        em.flush();
        em.clear();
        TokenResponse res = authService.login(new LoginRequest("reissue@test.com", "password123!"));
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

        Member member = memberRepository.save(MemberFixture.create(uniqueEmail, "테스터닉"));
        em.flush();
        em.clear();

        TokenResponse loginRes = authService.login(new LoginRequest(uniqueEmail, "password123!"));
        String firstRefreshToken = loginRes.refreshToken();
        Thread.sleep(1000);
        authService.reissue(new ReissueRequest(firstRefreshToken));

        em.flush();
        em.clear();

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(firstRefreshToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RefreshToken이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("DB상에서 만료된 토큰으로 재발급 시도 시 예외 발생")
    void reissue_fail_db_expired() {
        Member member = memberRepository.findByEmail(testEmail).orElseThrow();

        RefreshToken savedToken = refreshTokenRepository.findValidByMemberId(member.getId())
                .orElseThrow();
        savedToken.updateToken(savedToken.getRefreshTokenHash(), java.time.LocalDateTime.now().minusHours(1));

        em.flush();
        em.clear();

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(validRefreshToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("저장된 RefreshToken이 없거나 만료되었습니다.");
    }

    @Test
    @DisplayName("로그아웃 성공 후 해당 토큰으로 재발급 시도 시 예외 발생")
    void logout_success_and_reissue_fail() {
        Member member = memberRepository.findByEmail(testEmail).orElseThrow();

        authService.logout(member.getId());

        em.flush();
        em.clear();

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(validRefreshToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("저장된 RefreshToken이 없거나 만료되었습니다.");
    }
}
