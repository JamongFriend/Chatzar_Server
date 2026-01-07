package Project.Chatzar.presentation.controller.webSocket;

import Project.Chatzar.Domain.message.Message;
import Project.Chatzar.application.ChatEventService;
import Project.Chatzar.application.MessageService;
import Project.Chatzar.config.webSocket.StompPrincipal;
import Project.Chatzar.presentation.dto.message.response.MessageResponse;
import Project.Chatzar.presentation.dto.message.response.SocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatStompController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handle(SocketMessage payload, Principal principal) {
        if (payload == null || payload.roomId() == null) {
            throw new IllegalArgumentException("roomId가 필요합니다.");
        }

        String type = (payload.type() == null) ? "SEND" : payload.type().trim();

        switch (type) {
            case "SEND" -> handleSend(payload, principal);
            case "JOIN" -> handleJoin(payload, principal);
            default -> throw new IllegalArgumentException("지원하지 않는 type 입니다: " + type);
        }
    }

    private void handleSend(SocketMessage payload, Principal principal) {
        Long senderId = ((StompPrincipal) principal).getMemberId();

        Message saved = messageService.sendMessage(payload.roomId(), senderId, payload.content());

        // 3) 구독자에게 브로드캐스트
        messagingTemplate.convertAndSend(
                "/sub/rooms/" + payload.roomId(),
                MessageResponse.from(saved)
        );
    }

    private void handleJoin(SocketMessage payload, Principal principal) {
        Long memberId = ((StompPrincipal) principal).getMemberId();

        messagingTemplate.convertAndSend(
                "/sub/rooms/" + payload.roomId(),
                payload
        );
    }

    private Long getSenderIdTemporary() {
        throw new IllegalStateException("senderId를 가져오는 로직이 필요합니다. (JWT/Principal 또는 payload)");
    }
}
