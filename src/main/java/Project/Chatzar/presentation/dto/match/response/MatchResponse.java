package Project.Chatzar.presentation.dto.match.response;

import Project.Chatzar.Domain.member.Member;

public record MatchResponse(
        boolean matched,
        Long matchId,
        Long chatRoomId,
        Long partnerId,
        String partnerNickname
) {
    public static MatchResponse waiting() {
        return new MatchResponse(false, null, null, null, null);
    }

    public static MatchResponse matched(Long matchId, Long chatRoomId, Member partner) {
        return new MatchResponse(true, matchId, chatRoomId, partner.getId(), partner.getNickname());
    }
}
