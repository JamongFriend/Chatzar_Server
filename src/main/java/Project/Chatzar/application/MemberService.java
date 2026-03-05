package Project.Chatzar.application;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.presentation.dto.member.JoinRequest;
import Project.Chatzar.presentation.dto.member.MemberResponse;
import Project.Chatzar.Domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(JoinRequest request) {
        validateDuplicateEmail(request.email());


        Member member = Member.createWithTag(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                request.age()
        );

        return memberRepository.save(member).getId();
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return MemberResponse.fromEntity(member);
    }

}
