package Project.Chatzar.presentation.dto.chatRoom.response;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;

public record ChatRoomResponse(
        Long roomId,
        Long otherMemberId,
        String otherNickname,
        String status
) {
    public static ChatRoomResponse from(ChatRoom room, Member me) {
        Member other = room.getMemberA().getId().equals(me.getId()) ? room.getMemberB() : room.getMemberA();
        return new ChatRoomResponse(room.getId(), other.getId(), other.getNickname(), room.getStatus().name());
    }
}
