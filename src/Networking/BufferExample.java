package Networking;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferExample {
    public static void main(String[] args) throws Exception{
        System.out.println("7바이트 크기 버퍼 생성");
        ByteBuffer buffer = ByteBuffer.allocateDirect(7); // ByteBuffer 생성
        printState(buffer);

        buffer.put((byte)10); // 데이터 저장 int 10 -> byte 10 타입캐스팅
        buffer.put((byte) 11);
        System.out.println();
        printState(buffer);


        buffer.put((byte) 12);
        buffer.put((byte) 13);
        buffer.put((byte) 14);
        System.out.println();
        printState(buffer);

        buffer.flip();
        System.out.println("flip() 실행");
        printState(buffer);

        buffer.get(new byte[3]); // Relative  get(byte[]) 바이트 배열의 인덱스 이동
        System.out.println("3바잍 읽은 후 ");
        printState(buffer);

        buffer.mark();
        System.out.println("현재 포지션 마크");

        buffer.get(new byte[2]);
        System.out.println("2바이트 읽은 후 ");
        printState(buffer);

        buffer.reset(); // mark position 으로 position 이동
        System.out.println("mark position 으로 이동");
        printState(buffer);

        buffer.rewind(); // position 0으로 이동
        System.out.println("rewind 실행");
        printState(buffer);

        buffer.clear();
        printState(buffer);
    }

    public static void printState(Buffer buffer){
        System.out.println("\tposition"+ buffer.position() + ",");
        System.out.println("\tlimit" +buffer.limit()+",");
        System.out.println("\tcapacity"+buffer.capacity());
    }
}
