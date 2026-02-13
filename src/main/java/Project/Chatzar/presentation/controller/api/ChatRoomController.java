package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.application.ChatRoomService;
import Project.Chatzar.presentation.dto.chatRoom.response.ChatRoomResponse;
import Project.Chatzar.presentation.dto.chatRoom.request.CreateRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponse> createRoom (@AuthenticationPrincipal Long memberId,
                                                        @RequestBody CreateRoomRequest request
    ) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        Member other = memberRepository.findById(request.otherMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        ChatRoom room = chatRoomService.createRoom(me, other);

        return ResponseEntity.ok(ChatRoomResponse.from(room, me));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getRoom(@AuthenticationPrincipal Long memberId,
                                    @PathVariable Long roomId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        ChatRoom room = chatRoomService.getRoom(roomId);

        if (!room.isParticipant(me)) {
            throw new IllegalArgumentException("채팅방 참가자가 아닙니다.");
        }

        return ResponseEntity.ok(ChatRoomResponse.from(room, me));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> myRooms(@AuthenticationPrincipal Long memberId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));

        List<ChatRoomResponse> rooms = chatRoomService.getMyRooms(me)
                .stream().map(r -> ChatRoomResponse.from(r, me))
                .toList();

        return ResponseEntity.ok(rooms);
    }

    // 랜덤 채팅 종료 후 방을 나갔을 때 방 잠금으로 변환
    @PostMapping("/{roomId}/close")
    public ResponseEntity<Void> closeRoom(@AuthenticationPrincipal Long memberId,
                                          @PathVariable Long roomId) {
        chatRoomService.closeRandomChatRoom(roomId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@AuthenticationPrincipal Long memberId,
                                           @PathVariable Long roomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id=" + memberId));
        chatRoomService.deleteRoom(roomId, member);
        return ResponseEntity.noContent().build();
    }

}
