package Project.Chatzar.presentation.dto.message.response;

import Project.Chatzar.Domain.message.Message;

import java.time.LocalDateTime;

public record MessageResponse(
        Long messageId,
        Long roomId,
        Long senderId,
        String content,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getMember().getId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
