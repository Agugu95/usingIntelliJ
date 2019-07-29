package Networking;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferConversionExample {
    public static void main(String[] args) throws Exception{
        Charset charset = Charset.defaultCharset(); // UTF-16 유니코드 인코딩

        // String -> ByteBuffer
        String data = "안녕하세요요요요요요";
        ByteBuffer byteBuffer;
        byteBuffer = charset.encode(data);
        System.out.println(byteBuffer);

        // ByteBuffer -> String
        data = charset.decode(byteBuffer).toString();
        System.out.println(data);

    }
}
