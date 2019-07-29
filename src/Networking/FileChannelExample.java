package Networking;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileChannelExample {
    public static void main(String[] args)  throws Exception{
        Path path = Paths.get("C:/TempT/fileChannel.txt");
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        String data = "안녕하세요";
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode(data);
        int ByteCount = fileChannel.write(byteBuffer);
        System.out.println("파일: " +ByteCount);
        fileChannel.close();

        fileChannel = FileChannel.open(path, StandardOpenOption.READ); // 파일 채널  열음
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
        int byteCount;
        while(true) {
            byteCount = fileChannel.read(byteBuffer1); // 바이트 버퍼만큼 데이터를 fileChannel로 100바이트 만큼 읽기
            System.out.println(byteCount); // 100바이트보다 파일 크기가 작기때문에 읽은 크기만큼만 반환 15
            if(byteCount == -1){
                break;
            }
            System.out.println(byteBuffer1); // 15개를 읽었음으로 position 15까지 증가, position 15 limit 100
            byteBuffer1.flip(); // 바이트를 읽을 때 증가 된 position 초기화, limit을 현재 position으로 변환
            data += charset.decode(byteBuffer1).toString(); // position 0부터 limit 15 까지 읽고, 디코딩
            System.out.println(byteBuffer1); // 읽으면서 position 증가 현재 position 15, limit 15
            byteBuffer1.clear();
            System.out.println(byteBuffer1); // position과 limit 초기화
        }
        fileChannel.close();
        System.out.println("읽은 파일: " + data);

    }
}
