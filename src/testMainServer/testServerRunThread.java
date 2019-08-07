package testMainServer;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testServerRunThread {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public ServerSocketChannel serverSocketChannel;
    public SocketChannel socketChannel;
    protected static List<testServerThread> list = new Vector<>();

    public void start() {
        // serverSocket 생성, 포트 바인딩
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress("localhost",8080));
        } catch (Exception e) {
            if(serverSocketChannel.isOpen()) {
                e.printStackTrace();
                stop();
            }
        }

        // Runnable 인터페이스를 구현한 작업 스레드 생성
        // while loop를 통해 accpet() 블로킹 메소드를 실행, 소켓 생성(4개 스레드 제한, 스레드풀 작업)
        Runnable runnable = () -> {
            System.out.println("서버 구동");
            while (true) {
                try {
                    // 연결요청 확인 시 socketChannel 생성
                    socketChannel = serverSocketChannel.accept();
                    System.out.println("연결: " + socketChannel.getRemoteAddress());

                    // 연결 된 sokcetChannel로 객체 생성
                    testServerThread client = new testServerThread(socketChannel);

                    // 생성 된 객체 리스트에 저장
                    list.add(client);
                    System.out.println("클라이언트 목록" + list);
                } catch (Exception e){
                    if (serverSocketChannel.isOpen()){
                        e.printStackTrace();
                        stop();
                    }
                    break;
                }
            }
        };
        executorService.submit(runnable);
    }


    public void stop() {
        try {
            // 리스트에 담긴 클라이언트 객체를 제거, 열려있는 소켓들을 종료
            for (int i = 0; i<list.size(); i++){
                list.remove(i);
                System.out.println("클라이언트 리스트: " + list.toString());
            }
            socketChannel.close();
            serverSocketChannel.close();
        } catch (Exception e){
            System.out.println(e + ": 소켓 종료 중 예외 발생");
        }
    }
}
