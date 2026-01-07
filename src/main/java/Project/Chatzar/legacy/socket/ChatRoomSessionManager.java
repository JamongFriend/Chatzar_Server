package Project.Chatzar.legacy.socket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// “초기에는 raw socket으로 구현했고, 이후 Spring WebSocket으로 리팩토링”
public class ChatRoomSessionManager {
    // 채팅방(roomId)마다 현재 접속 중인 소켓 세션(ClientHandler)들을 관리하고, 그 방에 메시지를 뿌려주는(브로드캐스트) 관리자
    // roomId -> 현재 방에 연결된 클라이언트들
    private final ConcurrentHashMap<Long, Set<ClientHandler>> roomSessions = new ConcurrentHashMap<>();

    // memberId -> 참가 중인 roomId 목록(QUIT 시 한번에 정리)
    private final ConcurrentHashMap<Long, Set<Long>> memberRooms = new ConcurrentHashMap<>();

    public void join(Long roomId, Long memberId, ClientHandler clientHandler) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(clientHandler);
        memberRooms.computeIfAbsent(memberId, k -> ConcurrentHashMap.newKeySet()).add(roomId);
    }

    public void leaveRooms(Long memberId, ClientHandler clientHandler) {
        Set<Long> rooms = memberRooms.remove(memberId);
        if (rooms == null) return;

        for (Long roomId : rooms) {
            Set<ClientHandler> handlers = roomSessions.get(roomId);
            if (handlers == null) continue;

            handlers.remove(clientHandler);

            if (handlers.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    public void broadcast(Long roomId, String payload) {
        Set<ClientHandler> handlers = roomSessions.get(roomId);
        if (handlers == null) return;

        for (ClientHandler h : handlers) {
            h.sendMessage(payload);
        }
    }
}
