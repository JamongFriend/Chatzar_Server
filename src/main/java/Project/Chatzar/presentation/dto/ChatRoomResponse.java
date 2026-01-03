package Project.Chatzar.presentation.dto;

import Project.Chatzar.Domain.chatRoom.ChatRoom;
import Project.Chatzar.Domain.member.Member;

public record ChatRoomResponse(
        Long roomId,
        Long otherMemberId
) {
    public static ChatRoomResponse from(ChatRoom room, Member me) {
        Member other = room.getMemberA().equals(me) ? room.getMemberB() : room.getMemberA();
        return new ChatRoomResponse(room.getId(), other.getId());
    }
}
