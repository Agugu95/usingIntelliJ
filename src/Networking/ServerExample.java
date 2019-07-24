package Networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerExample {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            while(true){
                System.out.println("Waited connect..");
                socket = serverSocket.accept(); // 서버 소켓에서 연결 수락
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("Successfully Connected" + isa.toString());

                byte[] byteArr = null;
                String message = null;
                // 읽어온 데이터를 저장할 배열, 보낼 문자열

                // 인풋 스트림 생성 -> 배열 생성 -> 스트림으로부터 배열을 읽어온 리턴값 저장 -> 새 문자열 생성
                InputStream is = socket.getInputStream(); // 연결 된 소켓으로부터 인풋 스트림 생성
                byteArr = new byte[100];
                int readByteCount = is.read(byteArr); // 상대방이 비정상 종료 시 IOException
                if (readByteCount == -1){
                    throw  new IOException();
                }
                message = new String(byteArr, 0, readByteCount, "UTF-8");
                System.out.println("읽은 데이터: " + message);

                // 아웃풋 스트림 생성 -> 메세지 생성 -> 바이트 배열에 인코딩하여 저장 -> 쓰기 -> 비우기
                OutputStream os = socket.getOutputStream();
                message = "Hello Client";
                byteArr = message.getBytes("UTF-8");
                os.write(byteArr);
                os.flush();
                os.close(); is.close();
                socket.close();

            }
        } catch (Exception e) { // 정상적 종료 예외처리
            try {
                socket.close(); // 비정상 종료 예외처리
            } catch (Exception e2){

            }
        }

        if (!serverSocket.isClosed()){
            try{
                serverSocket.close();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}
