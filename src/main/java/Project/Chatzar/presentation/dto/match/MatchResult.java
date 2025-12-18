package Project.Chatzar.presentation.dto.match;

import Project.Chatzar.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchResult {
    private boolean matched;
    private Long matchId;
    private Long chatRoomId;

    private Long partnerId;
    private String partnerNickname;

    public static MatchResult waiting() {
        return new MatchResult(false, null, null, null, null);
    }

    public static MatchResult matched(Long matchId, Long chatRoomId, Member partner) {
        return new MatchResult(true, matchId, chatRoomId, partner.getId(), partner.getNickname());
    }
}
