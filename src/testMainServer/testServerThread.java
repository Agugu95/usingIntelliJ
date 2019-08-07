package testMainServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testServerThread {
    private static final String UTF_8 = "UTF-8";
    private static SocketChannel socketChannel;
    private static ExecutorService executorService = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public testServerThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        receive();
    }

    public void receive() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    Charset charset = Charset.forName(UTF_8);
                    int byteCount = socketChannel.read(byteBuffer);
                    if (byteCount == -1) {
                        throw new IOException();
                    }
                    System.out.println("들어온 요청: " + socketChannel.getRemoteAddress());

                    byteBuffer.flip();
                    String data = charset.decode(byteBuffer).toString();
                    System.out.println(socketChannel+ "로 부터 받은 데이터: " + data);

                    for (testServerThread client : testServerRunThread.list) {
                        client.send(data);
                    }

                } catch (Exception e) {
                    testServerRunThread s = new testServerRunThread();
                    try {
                        s.stop();
                        s.socketChannel.close();
                        System.out.println(this + "의 소켓이 비정상 종료되었습니다.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
        };
        executorService.submit(runnable);
    }

    public void send(String data){
        Runnable runnable = () -> {
            try {
                System.out.println("연결된 소켓: " + socketChannel);
                Charset charset = Charset.forName(UTF_8);
                ByteBuffer byteBuffer = charset.encode(data);
                socketChannel.write(byteBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executorService.submit(runnable);
    }
}

