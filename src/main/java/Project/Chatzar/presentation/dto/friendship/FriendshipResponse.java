package Project.Chatzar.presentation.dto.friendship;

public record FriendshipResponse(
        Long friendshipId,
        Long requesterId,
        String requesterName,
        String requesterTag,
        Long requesterAge,
        String status
) {
}
