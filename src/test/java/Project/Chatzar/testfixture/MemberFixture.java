package Project.Chatzar.testfixture;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberStatus;

public class MemberFixture {
    public static Member create(String email, String nickname) {
        return new Member(
                "테스트유저",
                email,
                "password123!",
                nickname,
                "27",
                MemberStatus.ACTIVE
        );
    }
}
