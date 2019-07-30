package Networking.tcpBlockingChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientExample {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            System.out.println("연결요청");
            socketChannel.connect(new InetSocketAddress(5000));
            System.out.println("연결 성공");
            // 여기까지 소켓 채널 연결

            // ByteBuffer를 이용한 데이터 쓰기
            ByteBuffer byteBuffer = null;
            Charset charset = Charset.forName("UTF-8"); // UTF-8셋

            byteBuffer = charset.encode("안녕하세요"); // 지정 된 charset으로 인코드하여 저장
            socketChannel.write(byteBuffer);  // 저장 된 byteBuffer를 쓰기

            // ByteBuffer를 이용한 데이터 읽기, 블로킹 상태
            byteBuffer = ByteBuffer.allocate(100); // 한번에 읽을 데이터 크기 100byte
            int byteCount = socketChannel.read(byteBuffer); // 바이트 버퍼 크기만큼 읽고, 읽은 수 저장
            byteBuffer.flip(); // 읽으며 증가한 position 초기화, limit은 데이터의 최대 값
            String message = charset.decode(byteBuffer).toString(); // 지정 charset으로 디코드 된 바이트를 string으로 반환
            System.out.println("받기 성공 : " + message);

        } catch (Exception e){

        }
        if(socketChannel.isOpen()){
            try {
                socketChannel.close();
            } catch (IOException e1){

            }
        }
    }
}
