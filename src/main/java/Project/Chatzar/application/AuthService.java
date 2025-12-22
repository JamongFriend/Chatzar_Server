package Project.Chatzar.application;

import Project.Chatzar.config.JwtProperties;
import Project.Chatzar.Domain.auth.JwtTokenProvider;
import Project.Chatzar.Domain.auth.RefreshToken;
import Project.Chatzar.Domain.auth.RefreshTokenRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.auth.LoginRequest;
import Project.Chatzar.presentation.dto.auth.ReissueRequest;
import Project.Chatzar.presentation.dto.auth.SignUpRequest;
import Project.Chatzar.presentation.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Transactional
    public Long signUp(SignUpRequest request) {
        if(memberRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
        }

        if(memberRepository.existsByNickname(request.nickname())){
            throw new IllegalArgumentException("이미 사용 중인 닉네임 입니다.");
        }
        Member member = new Member(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                request.age(),
                MemberStatus.ACTIVE);

        return memberRepository.save(member).getId();
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));

        if(!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String access = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail());
        String refresh = jwtTokenProvider.createRefreshToken(member.getId(), member.getEmail());

        String refreshHash = passwordEncoder.encode(refresh);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(jwtProperties.refreshExpDays());

        repository.deleteByMemberId(member.getId());
        repository.save(new RefreshToken(member.getId(), refreshHash, expiresAt));

        return TokenResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        if (!jwtTokenProvider.isValid(request.refreshToken())) {
            throw new IllegalArgumentException("RefreshToken이 유효하지 않습니다.");
        }

        Long memberId = jwtTokenProvider.getMemberId(request.refreshToken());
        String email = jwtTokenProvider.getEmail(request.refreshToken());

        RefreshToken saved = repository.findValidByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("저장된 RefreshToken이 없거나 만료되었습니다."));

        // 저장된 해시와 비교
        if (!passwordEncoder.matches(request.refreshToken(), saved.getRefreshTokenHash())) {
            throw new IllegalArgumentException("RefreshToken이 일치하지 않습니다.");
        }

        String newAccess = jwtTokenProvider.createAccessToken(memberId, email);
        String newRefresh = jwtTokenProvider.createRefreshToken(memberId, email);

        saved.revoke(); // 이전 토큰 폐기(선택)

        LocalDateTime newExpiresAt = LocalDateTime.now().plusDays(jwtProperties.refreshExpDays());
        repository.save(new RefreshToken(memberId, passwordEncoder.encode(newRefresh), newExpiresAt));

        return TokenResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }

    @Transactional
    public void logout(Long memberId) {
        repository.deleteByMemberId(memberId);
    }
}
