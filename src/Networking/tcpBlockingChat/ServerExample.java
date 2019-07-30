package Networking.tcpBlockingChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerExample {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = null;
        try { // 소켓 채널을 생성하고 포트 바인딩 시 예외 처리를 하기 위한 try-catch
            serverSocketChannel = ServerSocketChannel.open(); // open 정적 메소드 통한 객체 생성
            serverSocketChannel.configureBlocking(true); // 명시적 블로킹 채널 명시
            serverSocketChannel.bind(new InetSocketAddress(5000)); // InetSocketAddress 객체로 포트 바인딩

            // 소켓 채널 형성을 위한 while loop
            while(true){
                System.out.println("SocketActivating.. .. .. ");
                SocketChannel socketChannel = serverSocketChannel.accept(); // 서버 소켓 채널에 요청이 들어오면 소켓 채널 반환
                InetSocketAddress isa = (InetSocketAddress) socketChannel.getRemoteAddress();
                System.out.println("Socket Connected : "+ isa.getHostName());

                // ByteBuffer를 이용한 데이터 쓰기
                ByteBuffer byteBuffer = null;
                Charset charset = Charset.forName("UTF-8");

                byteBuffer = charset.encode("Hello Client");
                socketChannel.write(byteBuffer);
                System.out.println("데이터 보냄");

                // ByteBuffer를 이용한 데이터 읽기, 블로킹 상태
                byteBuffer = ByteBuffer.allocate(100); // 100바이트씩 읽을 버퍼 생성
                int byteCount = socketChannel.read(byteBuffer); // 소켓 채널을 통해 버퍼만큼 읽고, 수를 반환
                if (byteCount == -1){
                    throw new IOException(); // 만약 소켓이 정상 close 되어있어 -1 반환 시, 확인 후 익셉션 발생
                }
                byteBuffer.flip(); // 읽으며 증가 된 버퍼 position 초기화
                String data = charset.decode(byteBuffer).toString();
                System.out.println("받은 데이터 : " + data);
            }


        } catch (Exception e){
            try {
                serverSocketChannel.close();
            } catch (Exception e2) {

            }
        }
        if (serverSocketChannel.isOpen()){ // ServerSocket이 열려있다면
            try {
                serverSocketChannel.close(); // ServerSocket 닫기
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }

    }
}
