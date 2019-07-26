package Networking;

import java.nio.ByteBuffer;

public class BufferSize {
    public static void main(String[] args) {
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(800 * 1024 * 1024);
        System.out.println("다이렉트 버퍼 생성");

        ByteBuffer nonDirectBuffer = ByteBuffer.allocate(800 * 1024 * 1024); // JVM 800MB 불가
        System.out.println("논다이렉트 버퍼 생성");
    }
}
