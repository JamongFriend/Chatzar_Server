package Project.Chatzar.testfixture;

import Project.Chatzar.Domain.match.Match;
import Project.Chatzar.Domain.member.Member;

public class MatchFixture {
    public static Match create(Member memberA, Member memberB) {
        return new Match(null, memberA, memberB);
    }

    public static Match createWithId(Long id, Member memberA, Member memberB) {
        return new Match(id, memberA, memberB);
    }
}
