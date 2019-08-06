package Networking.TCPChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatServerThread {
    // 클라이언트로부터 오는 데이터를 받을 작업 스레드
    private Socket ClientSocket;
    private String clientData;
    public void ChatServerThread(Socket socket){
        // 소켓이 연결되면서 들어온 소켓 객체를 this로 각 객체로 변환
        // 각 scoket 객체마다 별개의 Client 객체가 생성
        this.ClientSocket = socket;
    }
    Runnable runnable = () -> {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            clientData = reader.readLine();

        } catch (IOException e){

        }
    };

}
