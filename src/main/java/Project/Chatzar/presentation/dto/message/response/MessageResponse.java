package Project.Chatzar.presentation.dto.message.response;

import Project.Chatzar.Domain.message.Message;

import java.time.format.DateTimeFormatter;

public record MessageResponse(
        Long messageId,
        Long roomId,
        Long senderId,
        String senderNickname,
        String content,
        String createdAt
) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getMember().getId(),
                message.getMember().getNickname(),
                message.getContent(),
                message.getCreatedAt().format(formatter)
        );
    }
}
