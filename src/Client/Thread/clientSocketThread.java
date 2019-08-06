package Client.Thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class clientSocketThread {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    SocketChannel socketChannel;
    private String c_id;
    public void start(String id) {
        try {
            this.c_id = id;
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            System.out.println(c_id + ": 소켓이 연결되었습니다.");
            receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (socketChannel.isOpen()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receive() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    int byteCount = socketChannel.read(byteBuffer);
                    System.out.println("읽은 바이트 수: " + byteCount);
                    if (byteCount == -1) {
                        throw new IOException();
                    }
                    byteBuffer.flip();
                    Charset charset = Charset.forName("UTF-8");
                    String receiveData = charset.decode(byteBuffer).toString();
                    System.out.println(receiveData);
                    if (receiveData == null) {
                        System.out.println("서버로부터 데이터 못받음.");
                    }
                    System.out.println(">" + receiveData);
                } catch (Exception e) {
                    System.out.println(e + "서버 통신 에러");
                    stop();
                }
            }
        };
        executorService.submit(runnable);
    }

    public void send(String data){
        Runnable runnable = ()-> {
            try {
                System.out.println("보낼 데이터: " + data);
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer byteBuffer = charset.encode(data);
                System.out.println("보낼 인코딩 데이터: " + byteBuffer);
                socketChannel.write(byteBuffer);
            } catch (Exception e){
                System.out.println(e + "데이터 보내기 실패");
            }
        };
        executorService.submit(runnable);
    }
}
