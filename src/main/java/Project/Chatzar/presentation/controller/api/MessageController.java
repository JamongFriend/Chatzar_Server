package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.message.Message;
import Project.Chatzar.application.MessageService;
import Project.Chatzar.presentation.dto.message.response.MessageResponse;
import Project.Chatzar.presentation.dto.message.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms/{roomId}/messages")
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/recent")
    public ResponseEntity<List<MessageResponse>> recent(@AuthenticationPrincipal Long memberId,
                                                        @PathVariable Long roomId){
        List<MessageResponse> responses = messageService.getRecentMessages(roomId)
                .stream().map(MessageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> older(@AuthenticationPrincipal Long memberId,
                                                       @PathVariable Long roomId,
                                                       @RequestParam(required = false) Long before) {
        List<Message> messages = (before == null)
                ? messageService.getRecentMessages(roomId)
                : messageService.getOlderMessages(roomId, before);

        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> send(@AuthenticationPrincipal Long memberId,
                                                @PathVariable Long roomId,
                                                @RequestBody SendMessageRequest request) {
        Message saved = messageService.sendMessage(roomId, memberId, request.content());
        return ResponseEntity.ok(MessageResponse.from(saved));
    }
}