package Networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReciveExample extends Thread{
    public static void main(String[] args) throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket(5001); // 수신 소켓은 포트번호 바인딩

        Thread thread = new Thread(() -> {
             System.out.println("[수신시작]");
             try{
                 while(true){
                     DatagramPacket packet = new DatagramPacket(new byte[100], 100);
                     datagramSocket.receive(packet);

                     String data = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                     System.out.println("[받은 내용: " + packet.getAddress() + "]" + data);
                 }
             } catch (Exception e){
                 datagramSocket.close();
             }
         });
        thread.start();

        Thread.sleep(10000);
        datagramSocket.close();
    }
}
