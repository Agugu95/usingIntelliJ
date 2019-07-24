package Networking;

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    ExecutorService executorService; // 스레드 풀 ExecutorService
    ServerSocket serverSocket; // 서버 소켓
    List<Client> connections = new Vector<>(); // 연결된 클라이언트를 저장하는 connection 필드, 스레드에 안전한 vector(동기화)
    Socket socket = null;
    void startServer(){ // 서버 시작 시 호출
        // 스레드 풀 생성
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // 코어 프로세스 수만큼 스레드 생성

        // 서버소켓 생성 및 포트 바인딩
        try { // 예외처리를 위한 try-catch
            serverSocket = new ServerSocket(); // 서버 소켓 객체 생성
            serverSocket.bind(new InetSocketAddress("localhost", 5001)); // 생성자를 통한 포트바인딩
        } catch (Exception e){
            if(!serverSocket.isClosed()){ // 예외 발생 시 소켓이 정상종료가 아니면 stopserver 호출
                stopServer();
            }
            return;
        }

        // 연결 수락
        Runnable runnable = () -> {
            Platform.runLater(() -> {
                displayText("[서버시작]"); // 콘솔 창에 서버 시작 출력
            });
            while (true) {
                try {
                    socket = serverSocket.accept(); // 연결 수락 시 소켓 생성 (블로킹)
                    String message = "[연결 수락: " + socket.getRemoteSocketAddress() + ": "
                            + Thread.currentThread().getName() + "]";
                    Platform.runLater(() -> displayText(message));

                    // 소켓을 통해 만들어진 클라이언트 객체 저장
                    Client client = new Client(socket);
                    connections.add(client);

                    Platform.runLater(()-> displayText("[연결 개수: " + connections.size() + "]"));


                } catch (Exception e) {
                    if(!serverSocket.isClosed()){
                        stopServer();
                    }
                    break;
                }
            }
        };
        executorService.submit(runnable); // 스레드 풀에서 스레드 처리
    }

    void stopServer(){ // 서버 종료 시 호출
        try {
            Iterator<Client> iterator = connections.iterator();
            while(iterator.hasNext()){
                Client client = iterator.next();
                client.socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
            }
            if(executorService != null && !executorService.isShutdown()){
                executorService.shutdown();
            }
            Platform.runLater(() -> {
                displayText("[서버멈춤]");
            });
        } catch (Exception e){

        }
    }
    class Client{ // 연결된 클라이언트를 표현, 데이터 통신코드 포함
        Socket socket;
        public Client(Socket socket) {
            this.socket = socket;
            receive();
        }
        void receive() { // 데이터 받기
            Runnable runnable = (() -> {
                try{
                    while (true){
                        byte[] byteArr = new byte[100];
                        InputStream inputStream = socket.getInputStream();

                        // 클라이언트 비정상 종료 시 IOException 발생
                        int readByteCount = inputStream.read(byteArr);

                        // 클라이언트 정상 종료 (socket.close 호출)
                        if(readByteCount == -1) {
                            throw new IOException();
                        }
                        String message = "[요청처리: " + socket.getRemoteSocketAddress() + ": "
                                + Thread.currentThread().getName() + "]";
                        Platform.runLater(()->displayText(message));

                        // 문자열 디코딩
                        String data = new String(byteArr, 0, readByteCount, "UTF-8");
                        for(Client remote : connections){
                            remote.send(data);
                        }
                    }

                } catch (Exception e){
                    try {
                        connections.remove(Client.this); // 예외 발생 시 컬렉션에서 클라이언트 객체 제거
                        String message = "[클라이언트 통신 불가: " + socket.getRemoteSocketAddress() +
                                ": " + Thread.currentThread().getName() + "]";
                        Platform.runLater(()->displayText(message));
                        socket.close();
                    } catch (IOException e2){

                    }
                }
            });
            executorService.submit(runnable);
        }
        void send(String data){ // 데이터 보내기
            Runnable runnable = (()-> {
               try {
                   byte
               }
            });
        }


    }


    ///////////////////// UI 생성
    public void displayText(String text){
        System.out.println(text + "\n");

    }

}
