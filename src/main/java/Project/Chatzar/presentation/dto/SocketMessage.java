package Project.Chatzar.presentation.dto;

public record SocketMessage(
        String type,
        Long roomId,
        Long memberId,
        Long senderId,
        Long lastMessageId,
        String content
) {}
