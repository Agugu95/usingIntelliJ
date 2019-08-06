package Server.Thread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManagedClientThread {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    Charset charset = Charset.forName("UTF-8");
    // 리스트에 클라이언트를 넣고, 관리
    SocketChannel socketChannel;


    public ManagedClientThread(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        receive();
    }

    public void receive() {
        Runnable runnable = () -> {
            do {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    int byteCount = socketChannel.read(byteBuffer);
                    if (byteCount == -1) {
                        throw new IOException();
                    }
                    byteBuffer.flip();
                    String data = charset.decode(byteBuffer).toString();
                    if (data == null) {
                        System.out.println("받은 데이터 없음");
                    }

                    socketThread socketThread = new socketThread();
                    for (ManagedClientThread thread : socketThread.clients) {
                        thread.resending(data);
                    }

                } catch (Exception e) {

                }
            } while (true);
        };
        executorService.submit(runnable);
    }

    public void resending(String data) {
        Runnable runnable = () -> {
            try {
                ByteBuffer byteBuffer = charset.encode(data);
                socketChannel.write(byteBuffer);
            } catch (Exception e){

            }
        };
        executorService.submit(runnable);
    }

}
