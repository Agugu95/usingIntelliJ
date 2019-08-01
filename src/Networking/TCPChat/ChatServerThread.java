package Networking.TCPChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ChatServerThread { // 클라이언트의 접속 요청을 처리하는 작업 스레드
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

        Runnable runnable = () -> {
            while (true) {
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("연결 클라이언트: " + socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
