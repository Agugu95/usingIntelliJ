package Server;

import Server.Thread.socketThread;

import java.util.Scanner;

public class serverMain {

    // Main은 서버 객체의 생성, 서버의 실행, 종료를 관리
    public static void main(String[] args) {
        // main이 실행되면 start() 실행
        Scanner sc = new Scanner(System.in); // util 패키지 내에 있는 scanner 클래스(기본값 가능), inputStream 추가
        String command;
        socketThread server = null;
        try {
            server = new socketThread();
            server.start();
        } catch (Exception e) {
            System.out.println(e + "서버 실행 에러");
        }
        while(true) {
            System.out.print("SYSTEM : ");
            command = sc.nextLine();
            // 읽어온 라인이 stop이면 서버 종료
            if (command.equals("stop")) {
                    server.stop();
                    break;
            }
        }
    }
}
