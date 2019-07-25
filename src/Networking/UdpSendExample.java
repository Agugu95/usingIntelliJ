package Networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpSendExample {
    public static void main(String[] args) throws Exception{
        DatagramSocket datagramSocket = new DatagramSocket(); // 데이터 그램 소켓 객체 (발신소켓)
        System.out.println("[발신 시작]");

        for(int i =0; i<3; i++){
            String data = "메세지";
            byte[] bytes = data.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("localhost", 5001));
            datagramSocket.send(packet);
            System.out.println("보낸 바이트 수: " + bytes.length + "bytes");

        }
        System.out.println("[발신 종료]");
        datagramSocket.close();
    }
}
