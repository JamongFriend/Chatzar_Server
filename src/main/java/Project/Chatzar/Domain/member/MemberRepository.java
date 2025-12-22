package Project.Chatzar.Domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.net.ssl.SSLSession;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    Member save(Member member);

    Optional<Member> findById(Long memberId);
}
