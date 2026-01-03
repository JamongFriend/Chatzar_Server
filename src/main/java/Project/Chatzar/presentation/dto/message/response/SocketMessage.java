package Project.Chatzar.presentation.dto.message.response;

public record SocketMessage(
        String type,
        Long roomId,
        Long memberId,
        Long senderId,
        Long lastMessageId,
        String content
) {}
