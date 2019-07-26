package Networking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OSbyteOrder {
    public static void main(String[] args) {
        System.out.println("운영체제: " + System.getProperty("os.name"));
        System.out.println("Native ByteOrder: " + ByteOrder.nativeOrder());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100).order(ByteOrder.nativeOrder());
        // 바이트 버퍼를 생성 시 네이티브 오더로 순서 맞춤
    }
}
