package Project.Chatzar.config;

import Project.Chatzar.Service.MessageService;

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
            sendMessage("서버에 연결되었습니다. 메시지를 입력하세요.");

            String line;
            while((line = in.readLine()) != null){
                String[] tokens = line.split(":");
                String code = tokens[0];
                switch (code) {
                    case "SEND" -> {
                        Long roomId = Long.parseLong(tokens[1]);
                        Long senderId = Long.parseLong(tokens[2]);
                        String content = tokens[3];

                        messageService.sendMessage(roomId, senderId, content);
                    }
                    case "JOIN" -> {
                        Long roomId = Long.parseLong(tokens[1]);
                        Long memberId = Long.parseLong(tokens[2]);
                        chatRoomSessionManager.join(roomId, memberId, this);
                    }
                    case "QUIT" -> {
                        Long memberId = Long.parseLong(tokens[1]);
                        chatRoomSessionManager.leaveRooms(memberId, this);
                        return;
                    }
                }
            }
        } catch (IOException e){
            System.out.println("클라이언트 연결 오류: " + e.getMessage());
        } finally {
            close();
        }

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
}
