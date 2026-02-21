package Project.Chatzar.presentation.dto.friendship;

public record FriendshipResponse(
        Long friendshipId,
        String requesterName,
        String status
) {
}
