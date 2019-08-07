package testMainServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testMainC {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static SocketChannel socketChannel;
    private static final String UTF_8 = "UTF-8";

    public void start(){
        Runnable runnable = ()-> {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(true);
                socketChannel.connect(new InetSocketAddress("localhost",8080));
                receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        executorService.submit(runnable);
    }

    private void receive(){
        Runnable runnable = () -> {
            while (true) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                Charset charset = Charset.forName(UTF_8);
                try {
                    int byteCount = socketChannel.read(byteBuffer);
                    if (byteCount == -1) {
                        throw new IOException();
                    }
                    byteBuffer.flip();
                    String data = charset.decode(byteBuffer).toString();
                    System.out.println(data);
                } catch (Exception e) {
                    try {
                        socketChannel.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(runnable);
    }

    private void send(String data){
        Runnable runnable = () -> {
            try {
                Charset charset = Charset.forName(UTF_8);
                ByteBuffer byteBuffer = charset.encode(data);
                socketChannel.write(byteBuffer);
            } catch (Exception e){
                e.printStackTrace();
            }
        };
        executorService.submit(runnable);
    }
    public static void main(String[] args) {
        testMainC c = new testMainC();
        c.start();
        Scanner sc = new Scanner(System.in);
        String data;
        while(true) {
            System.out.print("> ");
            data = sc.nextLine();
            if (data != null){
                c.send(data);
            }
        }
    }
}
