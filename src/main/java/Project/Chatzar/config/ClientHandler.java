package Project.Chatzar.config;

import Project.Chatzar.application.MessageService;
import Project.Chatzar.presentation.dto.SocketMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class ClientHandler implements Runnable  {
    private final Socket socket;
    private final Server server;

    private final MessageService messageService;
    private final ChatRoomSessionManager chatRoomSessionManager;

    private BufferedReader in;
    private BufferedWriter out;

    // 라인 단위 JSON(NDJSON) 파싱용
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClientHandler(Socket socket, Server server, MessageService messageService, ChatRoomSessionManager chatRoomSessionManager) {
        this.socket = socket;
        this.server = server;
        this.messageService = messageService;
        this.chatRoomSessionManager = chatRoomSessionManager;

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException("스트림 생성 실패",e);
        }
    }

    @Override
    public void run() {
        try {
            sendMessage("OK:CONNECTED");

            String line;

            while((line = in.readLine()) != null){
                if (line.isBlank()) continue;

                SocketMessage msg;
                try {
                    msg = objectMapper.readValue(line, SocketMessage.class);
                } catch (Exception parseEx) {
                    sendMessage("ERROR:BAD_JSON:" + parseEx.getMessage());
                    continue; // 파싱 실패는 해당 요청만 버리고 루프 계속
                }

                String type = msg.type();
                if (type == null) {
                    sendMessage("ERROR:MISSING_TYPE");
                    continue;
                }
                try {
                    switch (type) {
                        case "JOIN" -> handleJoin(msg);
                        case "SEND" -> handleSend(msg);
                        case "OLDER" -> handleOlder(msg);
                        case "QUIT" -> {
                            handleQuit(msg);
                            return;
                        }
                        default -> sendMessage("ERROR:UNKNOWN_TYPE:" + type);
                    }
                } catch (Exception e) {
                    sendMessage("ERROR:" + type + ":" + safeMsg(e));
                }
            }
        } catch (Exception e){
            System.out.println("클라이언트 연결 오류: " + e.getMessage());
        } finally {
            close();
        }

    }

    private void handleJoin(SocketMessage msg) {
        Long roomId = msg.roomId();
        Long memberId = msg.memberId();

        if (roomId == null || memberId == null) {
            sendMessage("ERROR:JOIN:missing roomId/memberId");
            return;
        }

        chatRoomSessionManager.join(roomId, memberId, this);
        sendMessage("OK:JOIN:" + roomId);

        // 최근 30개 내려주기 (텍스트 포맷 유지)
        messageService.getRecentMessages(roomId).forEach(m -> {
            // MESSAGE:roomId:messageId:senderId:content
            String payload = "MESSAGE:" + roomId + ":" + m.getId() + ":" + m.getMember().getId() + ":" + m.getContent();
            sendMessage(payload);
        });
        sendMessage("OK:HISTORY_END:" + roomId);
    }

    private void handleSend(SocketMessage msg) {
        Long roomId = msg.roomId();
        Long senderId = msg.senderId();
        String content = msg.content() == null ? "" : msg.content();

        if (roomId == null || senderId == null) {
            sendMessage("ERROR:SEND:missing roomId/senderId");
            return;
        }

        var saved = messageService.sendMessage(roomId, senderId, content);

        // 방 참여자들에게 브로드캐스트
        String payload = "MESSAGE:" + roomId + ":" + saved.getId() + ":" + senderId + ":" + saved.getContent();
        chatRoomSessionManager.broadcast(roomId, payload);

        // 보낸 사람에게 ACK
        sendMessage("OK:SEND:" + roomId + ":" + saved.getId());
    }

    private void handleOlder(SocketMessage msg) {
        Long roomId = msg.roomId();
        Long lastMessageId = msg.lastMessageId();

        if (roomId == null || lastMessageId == null) {
            sendMessage("ERROR:OLDER:missing roomId/lastMessageId");
            return;
        }
        messageService.getOlderMessages(roomId, lastMessageId).forEach(m -> {
            String payload = "MESSAGE:" + roomId + ":" + m.getId() + ":" + m.getMember().getId() + ":" + m.getContent();
            sendMessage(payload);
        });

        sendMessage("OK:OLDER_END:" + roomId);
    }

    private void handleQuit(SocketMessage msg) {
        Long memberId = msg.memberId();
        if (memberId == null) {
            sendMessage("ERROR:QUIT:missing memberId");
            return;
        }

        chatRoomSessionManager.leaveRooms(memberId, this);
        sendMessage("OK:QUIT");
    }

    public void sendMessage(String message){
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println("메시지 전송 실패: " + e.getMessage());
        }
    }

    private void close() {
        server.removeClient(this);
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            socket.close();
        } catch (IOException e) {
            // ignore
        }
    }

    private String safeMsg(Exception e) {
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }
}
