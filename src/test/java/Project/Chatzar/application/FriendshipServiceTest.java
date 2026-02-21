package Project.Chatzar.application;

import Project.Chatzar.Domain.chatRoom.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FriendshipServiceTest {
    @Autowired FriendshipService friendshipService;
    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("친구 요청을 수락하면 잠겨있던 랜덤 채팅방이 활성화되어야 한다")
    void acceptFriendRequest_unlocksRoom() {

    }

}
