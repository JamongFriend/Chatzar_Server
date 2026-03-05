package Project.Chatzar.testfixture;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFixture {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static final String ENCODED_PASSWORD = encoder.encode("password123!");

    public static Member create(String email, String nickname) {
        String tag = String.format("%04d", (int) (Math.random() * 9999) + 1);

        return new Member(
                "테스트유저",
                email,
                ENCODED_PASSWORD, // 여기서 진짜 암호화를 수행
                nickname,
                tag,
                27L,
                MemberStatus.ACTIVE
        );
    }
}
