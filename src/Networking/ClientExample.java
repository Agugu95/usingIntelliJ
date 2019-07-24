package Networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientExample {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(); // 소켓 객체 생성
            System.out.println("연결 요청");
            socket.connect(new InetSocketAddress("localhost", 5001)); // 연결 전까지 블로킹
            System.out.println("연결 성공");

            byte [] bytearr = null;
            String message = null;

            OutputStream os = socket.getOutputStream(); // 연결 된 소켓으로부터 아웃풋 스트림 생성
            message = "Hello S E R V E R";
            bytearr = message.getBytes("UTF-8"); // 문자열로부터 UTF-8형식 인코딩 바이트 가져옴
            os.write(bytearr); // write메소드를 통해 UTF-8형식 데이터 전송
            os.flush();
            System.out.println("데이터 전송");

            InputStream is = socket.getInputStream(); // 연결 된 소켓으로부터 인풋 스트림 생성
            bytearr = new byte[100]; // 한번에 읽을 바이트 배열 크기
            int readByteCount =  is.read(bytearr); // 바이트 배열에 읽어온 크기만큼 리턴
            message = new String(bytearr, 0, readByteCount, "UTF-8" );
            System.out.println("받은 데이터: " + message);

            os.close(); is.close();

        } catch (UnknownHostException une) { // 잘못된 IP 주소

        } catch (IOException ioe){ // 주어진 포트 접속 불가

        }
        if (!socket.isClosed()){
            try{
                socket.close();
            } catch (Exception e2){

            }
        }
    }
}
