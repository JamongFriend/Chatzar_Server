package Project.Chatzar.application;

import Project.Chatzar.Domain.auth.AuthRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.presentation.dto.LoginRequest;
import Project.Chatzar.presentation.dto.MemberResponse;
import Project.Chatzar.presentation.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    @Transactional
    public Long register(RegisterRequest request) {
        if(authRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
        }

        if(authRepository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("이미 사용 중인 닉네임 입니다.");
        }
        Member member = new Member(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getAge(),
                MemberStatus.ACTIVE);

        return authRepository.save(member).getId();
    }

    public MemberResponse login(LoginRequest request) {
        Member member = authRepository.findByEmail(request.getEmail())
                .orElseThrow(()
                        -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));

        if(!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return MemberResponse.fromEntity(member);
    }
}
