package Server.Thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class socketThread {

    // Thread를 Runnable을 이용한 스레드 풀 구성하기 위한 ExectutorService
    private static ExecutorService executorService;
    // 채널을 이용한 소켓 객체
    public static ServerSocketChannel serverSocketChannel;
    public static SocketChannel socketChannel;

    private ManagedClientThread clientThread;
    public List<ManagedClientThread> clients = new Vector<>();
    public void showConsole(String message){ // 콘솔에 예외 메세지 띄우기
        System.out.println(message);
    }

    // ServerSocket을 열고 연결을 받는 작업 메소드
    public void start() throws IOException {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);

        // 호스트 주소 객체를 얻기 위한 InetSocket
        InetSocketAddress inet = new InetSocketAddress("localhost", 8080);

        // LocalHost의 이름과 주소
        String HOST_NAME = inet.getHostName();
        String HOST_ADDRESS = String.valueOf(inet.getAddress());
        serverSocketChannel.bind(inet);
        System.out.println("서버 동작\n" + "호스트: " + HOST_NAME + "\n호스트 주소: " + HOST_ADDRESS);

        Runnable runnable = () -> {
            while (true) {
                try {
                    socketChannel = serverSocketChannel.accept();
                    showConsole(socketChannel.getRemoteAddress().toString() + "연결");
                    clientThread = new ManagedClientThread(socketChannel);
                    clients.add(clientThread);
                    System.out.println(clients);
                } catch (Exception e) {
                    try {
                        socketChannel.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    showConsole(e + ": 클라이언트 소켓 생성 실패 ");
                    break;
                }
            }
        };
        executorService.submit(runnable);
    }

    public void stop() {
        try {
            Stream<ManagedClientThread> stream = clients.stream();
            if (stream != null) {
                stream.forEach((clients) -> {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        System.out.println(e + "클라이언트 소켓 종료에 실패하였습니다.");
                    }
                });
            } else {
                System.out.println("클라이언트 리스트가 비어있습니다.");
            }
            if (serverSocketChannel != null && serverSocketChannel.isOpen()){
                serverSocketChannel.close();
            }
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
            showConsole("서버와 연결 된 클라이언트 : " + clients.size() + "\n연결 된 소켓을 종료하였습니다.");

        } catch (Exception e){
            System.out.println(e + "아직 소켓이 열려있거나, 소켓이 비어있지 않습니다.");
        }
    }
}
