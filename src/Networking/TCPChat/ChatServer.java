package Networking.TCPChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class ChatServer {
    ExecutorService executorService;
    static List<ChatClient> clients = new Vector<>();
    static String HOST_NAME;
    static String HOST_ADDRESS;

    public static void main(String[] args) {
        try {

        } catch (Exception e) {

        }
    }

    public void start() throws IOException { // ServerSocket을 열고 연결을 받는 작업 스레드

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress inet = new InetSocketAddress("localhost", 8080);
        HOST_NAME = inet.getHostName();
        HOST_ADDRESS = String.valueOf(inet.getAddress());
        serverSocketChannel.bind(inet);
        System.out.println("서버 동작\n" + "호스트: " + HOST_NAME + "\n호스트 주소: " + HOST_ADDRESS);


        Runnable runnable = () -> {
            while (true) {
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("연결 클라이언트: " + HOST_NAME);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(runnable);
    }

    public void stop() {

    }


    public void send() { // 클라이언트로부터 온 메세지를 모든 클라이언트에게 재 전송

    }

    public void receive() { // 받은 데이터를 send

    }

}
