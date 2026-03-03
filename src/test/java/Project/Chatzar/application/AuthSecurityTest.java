package Project.Chatzar.application;

import Project.Chatzar.Domain.auth.JwtTokenProvider;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.presentation.dto.auth.request.ReissueRequest;
import Project.Chatzar.testfixture.JwtTokenProviderFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthSecurityTest {
    @Autowired AuthService authService;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Value("${jwt.secret}") String secretKey; // 실제 환경 설정의 키를 가져옴

    JwtTokenProviderFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new JwtTokenProviderFixture(secretKey);
    }

    @Test
    @DisplayName("만료된 토큰으로 재발급 시도 시 401 또는 예외 발생")
    void reissue_fail_expired() {
        String expiredToken = fixture.createExpiredToken(1L, "test@test.com");

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(expiredToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RefreshToken이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("서명이 위조된 토큰으로 재발급 시도 시 예외 발생")
    void reissue_fail_invalidSignature() {
        String fakeToken = fixture.createInvalidSignatureToken(1L, "test@test.com");

        assertThatThrownBy(() -> authService.reissue(new ReissueRequest(fakeToken)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RefreshToken이 유효하지 않습니다.");
    }

}
