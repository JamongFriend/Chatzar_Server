package Project.Chatzar.presentation.dto.friendship;

import Project.Chatzar.Domain.member.Member;

public record FriendListResponse(
        Long friendshipId,
        Long friendId,
        String nickname,
        Long age
) {
    public static FriendListResponse of(Long friendshipId, Member friend) {
        return new FriendListResponse(
                friendshipId,
                friend.getId(),
                friend.getNickname(),
                friend.getAge()
        );
    }
}
