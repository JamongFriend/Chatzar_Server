package Project.Chatzar.infrastructure.member;

import Project.Chatzar.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNicknameAndTag(String nickname, String tag);

    boolean existsByNicknameAndTag(String nickname, String tag);
}
