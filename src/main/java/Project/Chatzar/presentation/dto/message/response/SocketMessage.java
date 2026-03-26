package Project.Chatzar.presentation.dto.message.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SocketMessage(
        String type, // SEND, JOIN, OLDER, QUIT
        Long roomId,
        Long memberId,
        Long senderId,
        Long lastMessageId,
        String content
) {}
