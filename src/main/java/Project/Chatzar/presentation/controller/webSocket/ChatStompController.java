package Project.Chatzar.presentation.controller.webSocket;

import Project.Chatzar.Domain.message.Message;
import Project.Chatzar.application.MessageService;
import Project.Chatzar.presentation.dto.message.response.MessageResponse;
import Project.Chatzar.presentation.dto.message.response.SocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatStompController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handle(SocketMessage payload) {
        if (payload == null || payload.roomId() == null) {
            throw new IllegalArgumentException("roomId가 필요합니다.");
        }

        String type = (payload.type() == null) ? "SEND" : payload.type().trim();

        switch (type) {
            case "SEND" -> handleSend(payload);
            case "JOIN" -> handleJoin(payload);
            default -> throw new IllegalArgumentException("지원하지 않는 type 입니다: " + type);
        }
    }

    private void handleSend(SocketMessage payload) {
        Long roomId = payload.roomId();

        // ✅ 지금 단계(임시): senderId를 어딘가에서 얻어야 함
        // - 최종: Principal/JWT에서 꺼냄
        // - 임시: payload에 senderId 필드를 추가해서 받기(권장X지만 개발엔 가능)
        Long senderId = getSenderIdTemporary(); // TODO 교체 필요

        Message saved = messageService.sendMessage(roomId, senderId, payload.content());

        // 3) 구독자에게 브로드캐스트
        messagingTemplate.convertAndSend(
                "/sub/rooms/" + roomId,
                MessageResponse.from(saved)
        );
    }

    private void handleJoin(SocketMessage payload) {
        Long roomId = payload.roomId();

        messagingTemplate.convertAndSend(
                "/sub/rooms/" + roomId,
                payload
        );
    }

    private Long getSenderIdTemporary() {
        throw new IllegalStateException("senderId를 가져오는 로직이 필요합니다. (JWT/Principal 또는 payload)");
    }
}
