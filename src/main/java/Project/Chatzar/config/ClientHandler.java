package Project.Chatzar.config;

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class ClientHandler implements Runnable  {
    private final Socket socket;
    private final Server server;

    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

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
                System.out.println("수신 [" + socket.getRemoteSocketAddress() + "]: " + line);

                server.broadcast(line, this);
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
