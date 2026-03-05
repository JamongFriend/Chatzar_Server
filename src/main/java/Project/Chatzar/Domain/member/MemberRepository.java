package Project.Chatzar.Domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.net.ssl.SSLSession;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByNicknameAndTag(String nickname, String tag);

    boolean existsByNicknameAndTag(String nickname, String tag);
}
