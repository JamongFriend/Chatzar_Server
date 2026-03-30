package Project.Chatzar.application;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.presentation.dto.member.JoinRequest;
import Project.Chatzar.presentation.dto.member.MemberResponse;
import Project.Chatzar.presentation.dto.member.SignupResponse;
import Project.Chatzar.Domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse join(JoinRequest request) {
        validateDuplicateEmail(request.email());

        String uniqueTag = generateUniqueTag(request.nickname());

        Member member = Member.createWithTag(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                uniqueTag,
                request.age()
        );

        memberRepository.save(member);
        
        return SignupResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return MemberResponse.fromEntity(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, Project.Chatzar.presentation.dto.member.ProfileUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        
        member.updateNickname(request.nickname());
        return MemberResponse.fromEntity(member);
    }

    private String generateUniqueTag(String nickname) {
        Random random = new Random();
        String tag;

        for (int i = 0; i < 10; i++) {
            tag = String.format("%04d", random.nextInt(9999) + 1);
            if (!memberRepository.existsByNicknameAndTag(nickname, tag)) {
                return tag;
            }
        }
        throw new IllegalStateException("다른 닉네임을 사용해주세요.");
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
}
