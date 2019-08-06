package Networking.TCPChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ChatServer {
    // Thread를 Runnable을 이용한 스레드 풀 구성하기 위한 ExectutorService
    private static ExecutorService executorService;
    // 채널을 이용한 소켓 객체
    private static ServerSocketChannel serverSocketChannel;
    private static SocketChannel socketChannel;
    // 멀티 스레딩을 위한 동기화 ArrayList, 클라이언트 객체의 리스트를 만드는 용도
    private static List<ChatClient> clients = new Vector<>();

    public void showConsole(String message){ // 콘솔에 예외 메세지 띄우기
        System.out.println(message);
    }


    public static void main(String[] args) {
        // main이 실행되면 start() 실행
        Scanner sc = new Scanner(System.in); // util 패키지 내에 있는 scanner 클래스(기본값 가능), inputStream 추가
        String command;
        ChatServer server = null;
        try {
            server = new ChatServer();
            server.start();
        } catch (Exception e) {
            System.out.println(e + "서버 실행 에러");
        }
        while(true) {
            System.out.print("SYSTEM : ");
            command = sc.nextLine();
            // 읽어온 라인이 stop이면 서버 종료, 아니면 전송
            if (command.equals("stop")) {
                server.stop();
                break;
            }
            server.send(command);
        }
    }

    public void start() throws IOException { // ServerSocket을 열고 연결을 받는 작업 스레드
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        InetSocketAddress inet = new InetSocketAddress("localhost", 8080); // 호스트 주소 객체를 얻기 위한 InetSocket
        // LocalHost의 이름과 주소
        String HOST_NAME = inet.getHostName();
        String HOST_ADDRESS = String.valueOf(inet.getAddress());
        serverSocketChannel.bind(inet);
        System.out.println("서버 동작\n" + "호스트: " + HOST_NAME + "\n호스트 주소: " + HOST_ADDRESS);


        Runnable runnable = () -> {
            while (true) {
                try {
                    socketChannel = serverSocketChannel.accept();
                    showConsole("연결 클라이언트: " + socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    showConsole(e + "클라이언트 소켓 생성 실패 ");
                }
            }
        };
        executorService.submit(runnable);
    }

    public void stop() {
        try {
            Stream<ChatClient> stream = clients.stream();
            stream.forEach((clients) -> {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    System.out.println(e + "클라이언트 소켓 종료에 실패하였습니다.");
                }
            });

            if (serverSocketChannel != null && serverSocketChannel.isOpen()){
                serverSocketChannel.close();
            }
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
            showConsole("서버와 연결 된 클라이언트 : " + clients.size() + "\n연결 된 소켓이 없습니다.");

        } catch (Exception e){
            System.out.println(e + "아직 소켓이 열려있거나, 소켓이 비어있지 않습니다.");
        }
    }


    public void send(String sendData) { // 클라이언트로부터 온 메세지를 모든 클라이언트에게 재 전송

    }

    public void receive() {
        // 클라이언트 객체에서 데이터를 받고, 배열에 저장, while 루프를 통한 작업 스레드에서 무한루프

    }

    public void withdrawClient() { // 클라이언트 List에서 클라이언트 제거

    }
}
