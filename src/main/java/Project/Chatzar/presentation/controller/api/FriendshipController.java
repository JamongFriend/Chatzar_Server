package Project.Chatzar.presentation.controller.api;

import Project.Chatzar.Domain.friendship.FriendshipRepository;
import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.application.FriendshipService;
import Project.Chatzar.presentation.dto.friendship.FriendshipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final MemberRepository memberRepository;

    @PostMapping("/request/{targetId}")
    public ResponseEntity<Void> sendRequest(@AuthenticationPrincipal Long memberId,
                                            @PathVariable Long targetId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        friendshipService.sendFriendRequest(me, targetId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendshipResponse>> getPendingRequests(
            @AuthenticationPrincipal Long memberId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return ResponseEntity.ok(friendshipService.getPendingRequests(me));
    }

    @PostMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptRequest(@AuthenticationPrincipal Long memberId,
                                              @PathVariable Long friendshipId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        friendshipService.acceptFriendRequest(friendshipId, me);

        return ResponseEntity.ok().build();
    }
}
