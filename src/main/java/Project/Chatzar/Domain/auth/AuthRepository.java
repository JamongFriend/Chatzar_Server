package Project.Chatzar.Domain.auth;

import Project.Chatzar.Domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    Member save(Member member);
}
