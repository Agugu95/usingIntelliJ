package Networking;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class FileChannelCopy {
    public static void main(String[] args) throws Exception {
        Path filePath = Paths.get("C:/TempT/KingScar.gif");
        Path tagetPath = Paths.get("C:/Tempt/CopyScar.gif");

        FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);

        FileChannel copyChannel = FileChannel.open(tagetPath,StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        int byteCount;
        while(true){
            byteBuffer.clear();
            byteCount = fileChannel.read(byteBuffer); // 버퍼 크기만큼 데이터 읽기
            if(byteCount == -1){
                break;
            }
            byteBuffer.flip();
            copyChannel.write(byteBuffer);
        }

        fileChannel.close(); copyChannel.close();
        System.out.println("채널 파일 복사 끝");

        Files.copy(filePath, tagetPath, StandardCopyOption.REPLACE_EXISTING); // 이미 타겟이 존재한다면 대체(복사 X)
        System.out.println("Files.copy() 복사");
    }
}
