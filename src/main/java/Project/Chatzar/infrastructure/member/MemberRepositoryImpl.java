package Project.Chatzar.infrastructure.member;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Optional<Member> findByNicknameAndTag(String nickname, String tag) {
        return memberJpaRepository.findByNicknameAndTag(nickname, tag);
    }

    @Override
    public boolean existsByNicknameAndTag(String nickname, String tag) {
        return memberJpaRepository.existsByNicknameAndTag(nickname, tag);
    }


}
