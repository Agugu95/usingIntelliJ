package testMainServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testMainS {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public ServerSocketChannel serverSocketChannel;
    public SocketChannel socketChannel;
    public static final String UTF_8 ="UTF-8";

    List<Client> list = new Vector<>();

    public void start() {
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(true);
                serverSocketChannel.bind(new InetSocketAddress(8080));
            } catch (Exception e) {
                if(serverSocketChannel.isOpen()) {
                }
            }
            Runnable runnable = () -> {
                System.out.println("서버 구동");
                while (true) {
                    try {
                        socketChannel = serverSocketChannel.accept();
                        System.out.println("연결: " + socketChannel.getRemoteAddress());
                        // 연결 된 sokcetChannel로 객체 생성
                        Client client = new Client(socketChannel);
                        // 생성 된 객체 리스트에 저장
                        list.add(client);
                        System.out.println("클라이언트 목록" + list);
                    } catch (Exception e){
                        if (serverSocketChannel.isOpen()){

                        }
                        break;
                    }
                }
            };
        executorService.submit(runnable);
    }

    public class Client {
        SocketChannel socketChannel;

        public Client(SocketChannel socketChannel) {
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
                    System.out.println("들어온 요청" + socketChannel.getRemoteAddress());

                    byteBuffer.flip();
                    String data = charset.decode(byteBuffer).toString();
                    System.out.println(socketChannel + "로 부터 받은 데이터: " + data);

                    for (Client client : list) {
                        client.send(data);
                    }
                } catch (IOException e) {
                    list.remove(Client.this);
                    e.printStackTrace();
                }
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

    public static void main(String[] args) {
        testMainS s = new testMainS();
        s.start();
    }
}
