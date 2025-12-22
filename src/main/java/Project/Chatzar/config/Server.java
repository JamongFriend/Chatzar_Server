package Project.Chatzar.config;

import Project.Chatzar.application.MessageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    int PORT = 8000;
    private final ServerSocket serverSocket;
    private final MessageService messageService;
    private final ChatRoomSessionManager chatRoomSessionManager;

    // 접속한 클라이언트들 관리
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public Server(int port,
                  MessageService messageService,
                  ChatRoomSessionManager chatRoomSessionManager) throws IOException {
        this.PORT = port;
        this.serverSocket = new ServerSocket(PORT);
        this.messageService = messageService;
        this.chatRoomSessionManager = chatRoomSessionManager;
    }

    //서버 시작
    public void start() {
        System.out.println("서버 시작 : " + PORT);
        try {
            while(true){
                System.out.println("클라이언트 접속 대기 중...");
                Socket socket = serverSocket.accept();
                System.out.println("Server accept");
                System.out.println("클라이언트 접속 : " + socket.getRemoteSocketAddress());

                // 클라이언트 하나당 스레드 하나
                ClientHandler clientHandler = new ClientHandler(socket, this, messageService, chatRoomSessionManager);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace(System.out);
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException e){

            }
        }
    }

    // 브로드케스트
    public void broadcast(String message, ClientHandler clientHandler){
        synchronized (clients) {
            for(ClientHandler client : clients){
                client.sendMessage(message);
            }
        }
    }

    // 연결 종료 시 리스트에서 제거
    public void removeClient(ClientHandler clientHandler){
        clients.remove(clientHandler);
        System.out.println("클라이언트 종료, 현재 접속: " + clients.size());
    }
}
