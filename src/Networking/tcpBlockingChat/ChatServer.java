package Networking.tcpBlockingChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    ExecutorService executorService;
    ServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<>(); // 추상 타입체크

    public void startServer(){
        executorService = Executors.newFixedThreadPool(Runtime // 가능한 프로세서만큼 스레드 생성
                .getRuntime()
                .availableProcessors()
        );

        // 실제 연결 작업
        try {
            // ServerSocketChannel 생성
            serverSocketChannel = ServerSocketChannel.open(); // serverSocketChannel 객체 생성
            serverSocketChannel.configureBlocking(true); // 블로킹임을 명시
            serverSocketChannel.bind(new InetSocketAddress(5000)); // InetSocketAddress를 통한 포트 바인딩

            // 작업 스레드를 생성하여 소켓 연결 작업 처리
            Runnable runnable = () -> {

                displayText("서버 시작");


                while(true){
                    // accept을 통한 blocking 소켓 채널 형성 작업
                    try {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        String message  = "연결 수락: " + socketChannel.getRemoteAddress() +
                                " : " + Thread.currentThread().getName();

                            displayText(message);


                        Client client = new Client(socketChannel); // 동기화 List에 클라이언트 채널로 연결 된 객체 저장
                        connections.add(client);

                            displayText("연결 개수: " + connections.size());

                    } catch (Exception e1) {
                        if(serverSocketChannel.isOpen()){
                            stopServer();
                            break;
                        }
                    }
                }
            };
            executorService.submit(runnable); // 스레드 풀에 작업 스레드 추가

        } catch (Exception e){
            if (serverSocketChannel.isOpen()){
                stopServer(); // serverSocketChannel이 열려있다면 stopServer 호출
                return;
            }
        }

    }

    public void stopServer(){
        // Client와 연결 된 모든 열려있는 소켓 채널 닫기
        try {
            Iterator<Client> iterator = connections.iterator();
            while(iterator.hasNext()){
                Client client = iterator.next();
                client.socketChannel.close();
            }

            // 모든 serverSocketChannel 닫기
            if(serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }

            // 스레드 풀 종료
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }


                displayText("서버 멈춤");


        } catch (Exception  e){
            e.printStackTrace();
        }
    }

    class Client {
        SocketChannel socketChannel;

        Client(SocketChannel socketChannel){
            this.socketChannel = socketChannel;
            receive();
        }

        void receive() {
            Runnable runnable = () -> {

                // 데이터를 받기 위한 무한 루프
                while(true) {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                        // 클라이언트 비정상 종료 시 IOException 발생
                        int readByteCount = socketChannel.read(byteBuffer);

                        // 클라이언트 정상 종료 시 반환 되는 -1 체크
                        if(readByteCount == -1){
                            throw new IOException(); // 종료를 위한 예외 발생
                        }

                        String message = "요청 처리: " + socketChannel.getRemoteAddress() +
                                " : " + Thread.currentThread().getName();

                            displayText(message);


                        // 받은 데이터 디코딩
                        byteBuffer.flip(); // 읽느라 증가된 position 초기화
                        Charset charset = Charset.forName("UTF-8"); // utf-8셋
                        String data = charset.decode(byteBuffer).toString();
                        System.out.println(data);

                        // 모든 클라이언트에게 전송
                        // for (Client client : connections){
                            send(data);
                        // }
                    } catch (Exception e){
                        e.printStackTrace();
                        try {
                            connections.remove(Client.this); // 현재 익셉션이 발생 된 Client 객체 제거
                            String message = "통신 불가 클라이언트 : " + socketChannel.getRemoteAddress()
                                    + " : " + Thread.currentThread().getName();

                                displayText(message);

                            socketChannel.close();
                        } catch (IOException e1) {
                            break;
                        }
                    }
                }
            };
            executorService.submit(runnable);
        }

        void send (String data){
            Runnable runnable = () -> {
                // 받은 data를 charSet으로 인코드하여 데이터 쓰기
                try {
                    Charset charset = Charset.forName("UTF-8");
                    ByteBuffer byteBuffer = charset.encode(data);
                    socketChannel.write(byteBuffer);
                } catch (Exception e){
                  try {
                      String message = "클라이언트 통신 불가: " +
                              socketChannel.getRemoteAddress() +" : "
                              + Thread.currentThread().getName();

                          displayText(message);

                  } catch (IOException e1){

                  }
                }
            };
            executorService.submit(runnable);
        }
    }

    // UI //
    public static void displayText(String data){
        System.out.println(data);
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("명령어 입력: ");
            String data = sc.nextLine();
            if (data.equals("start")) {
                chatServer.startServer();
            }
            if (data.equals("stop")) {
                chatServer.stopServer();
            }
            if (data.startsWith("send:")){

            }
        }

    }
}


