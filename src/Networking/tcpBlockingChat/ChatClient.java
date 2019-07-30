package Networking.tcpBlockingChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ChatClient {
    SocketChannel socketChannel; // Client는 socketChannel을 통한 connect만 필요

    void startClient () { // 클라이언트 구성
        Thread thread = new Thread(() -> {
           try {
               // SocketChannel 구성
               socketChannel = SocketChannel.open(); // open 메소드를 통한 socket 채널 객체
               socketChannel.configureBlocking(true);
               socketChannel.connect(new InetSocketAddress("localhost", 5000));

               displayText("연결완료: " + socketChannel.getRemoteAddress());
           } catch (Exception e) {
               displayText("서버통신 불가");
               if(socketChannel.isOpen()){
                   stopClient();
                   return;
               }
               receive(); // 서버에서 보낸 데이터 받기
           }
        });
        thread.start(); // 스레드 시작
    }

    void stopClient() { // 클라이언트 종료
        try {
            displayText("연결 끊음");
            if (socketChannel!= null && socketChannel.isOpen()){
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void  receive() { // 데이터 받기
        while(true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100); // 100바이트씩 읽기

                // 서버 비정상 종료 시 IOException
                int readByteCount = socketChannel.read(byteBuffer);
                System.out.println(readByteCount);
                // 서버 정상 종료시 -1 반환, IOException 발생시켜 종료
                if(readByteCount == -1){
                    throw new IOException();
                }

                // 받은 데이터 디코딩
                byteBuffer.flip(); // position 초기화
                Charset charset = Charset.forName("UTF-8");
                String data = charset.decode(byteBuffer).toString();
                displayText("받은 데이터: " + data);

            } catch (IOException e){
                displayText("서버 종료");
                stopClient();
                break;
            }
        }
    }
    void send (String data) { // 데이터 보내기
        Thread thread = new Thread(() -> {
            try {
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer byteBuffer = charset.encode(data);
                socketChannel.write(byteBuffer);
                displayText("텍스트 보내기 완료");
            } catch (Exception e){
                displayText("텍스트 보내기 실패");
                stopClient();
            }
        });
        thread.start();
    }

    // UI //
    public void displayText(String data) {
        System.out.println(data);
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        ChatClient chatClient =new ChatClient();
        while(true){
            String command = sc.nextLine();
            if (command.equals("start")) {
                chatClient.startClient();
            }
            if (command.equals("stop")) {
                chatClient.stopClient();
            }
            if(command.startsWith("send:")){
                chatClient.send(command);
            }
        }
    }
}
