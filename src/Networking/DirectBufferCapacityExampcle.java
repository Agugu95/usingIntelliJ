package Networking;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public class DirectBufferCapacityExampcle {
    public static void main(String[] args) {
        ByteBuffer byteBuffer  = ByteBuffer.allocateDirect(100); // 100 크기
        CharBuffer charBuffer = ByteBuffer.allocateDirect(100).asCharBuffer(); // 100 크기 50개의 char
        IntBuffer intBuffer = ByteBuffer.allocateDirect(100).asIntBuffer(); // 100 크기 25개의 int

        System.out.println(byteBuffer.capacity() + " " + charBuffer.capacity() + " " + intBuffer.capacity());
    }
}
