package Networking;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class ByteBufferToIntBuffer {
    public static void main(String[] args) throws Exception{
        // int[] -> intBuffer -> byteBuffer
        int[] intData = new int[]{1, 2, 3};
        IntBuffer intBuffer = IntBuffer.wrap(intData);
        ByteBuffer byteBuffer = ByteBuffer.allocate(intBuffer.capacity() * 4); // 4배 큰 바이트 버퍼 생성
        for ( int i= 0; i<intBuffer.capacity(); i++){
            byteBuffer.putInt(intBuffer.get(i));
        }
        byteBuffer.flip(); // 이동 된 position 초기화
        System.out.println(byteBuffer);

        // byteBuffer -> intBuffer -> int[]
        ByteBuffer readByteBuffer = byteBuffer;
        IntBuffer readIntBuffer = readByteBuffer.asIntBuffer();
        int[] readInt = new int[readIntBuffer.capacity()];
        // new int[] {readIntBuffer.capacity()} 를 했더니 new int[] {12}와 똑같아서 3만 나옴
        readIntBuffer.get(readInt);
        System.out.println(Arrays.toString(readInt));
    }
}
