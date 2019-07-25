package Networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    Socket socket;
    void startClient() {
        Thread thread = new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", 5001));

                displayText("[연결 완료: " + socket.getRemoteSocketAddress() + "]");

            } catch (Exception e){
                //Platform.runLater(() -> displayText("[서버 통신 안됨]"));
                if(!socket.isClosed()) { stopClient(); }
                return;
            }
            recive();
        });
        thread.start();
    }

    void stopClient() {
        try {
            //Platform.runLater(() -> {
                displayText("[연결 끊음]");
           // });
            if (socket != null && !socket.isClosed()){
                socket.close();
            }
        } catch (Exception e){

        }
    }

    void recive(){
        while(true){
            try{
                byte[] byteArr = new byte[100];
                InputStream is = socket.getInputStream();

                // 서버 비정상 종료 시
                int readByteCount = is.read(byteArr);

                // 서버가 정상적으로 socket.close() 호출
                if(readByteCount == -1) {
                    throw  new IOException();
                }

                String data = new String(byteArr, 0, readByteCount, "UTF-8");

                // Platform.runLater(()-> {
                    displayText(data);
                // });
            } catch (IOException e){
                // Platform.runLater(()-> {
                    displayText("[서버통신 안됨]");
                // });
                stopClient();
                break;
            }
        }
    }

    void send(String data){
        Thread thread = new Thread(() -> {
            try{
                byte[] byteArr = data.getBytes("UTF-8");
                OutputStream os = socket.getOutputStream();
                os.write(byteArr);
                os.flush();
                // Platform.runLater(()-> displayText("[보내기 완료]"));
            } catch (Exception e){
                    displayText("[서버 통신 안됨]");
                    stopClient();
            }
        });
        thread.start();
    }

    // 센드

    void displayText(String data){
        System.out.println(data);
    }


    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.startClient();
        Scanner sc = new Scanner(System.in);
        int command = sc.nextInt();
        while(true){
            System.out.print( "클라이언트 종료 1 또는 데이터를 입력 2 : " );
            switch (command) {
                case 1:
                    chatClient.stopClient();
                case 2:
                    String data = sc.nextLine();
                    if(data == "stopClient"){
                        chatClient.stopClient();
                        break;
                    }
                    chatClient.send(data);
            }
        }
    }
}
