package Project.Chatzar.Domain.chatRoom;

public enum ChatRoomStatus {
    MATCHED, // 랜덤 매칭 직후
    LOCKED,  // 매칭 종료 후 자동으로 잠금
    ACTIVE,  // 친구 성공 후 메세지 가능 상태
    DELETED // 채팅방 삭제
}
