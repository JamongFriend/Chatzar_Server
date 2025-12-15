package Project.Chatzar.Service;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.LoginRequest;
import Project.Chatzar.presentation.dto.MemberResponse;
import Project.Chatzar.presentation.dto.RegisterRequest;
import Project.Chatzar.Domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(RegisterRequest request) {
        if(memberRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
        }

        if(memberRepository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("이미 사용 중인 닉네임 입니다.");
        }
        Member member = new Member(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getAge(),
                MemberStatus.ACTIVE);

        return memberRepository.save(member).getId();
    }

    public MemberResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(()
                        -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));

        if(!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return MemberResponse.fromEntity(member);
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return MemberResponse.fromEntity(member);
    }

}
