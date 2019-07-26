package Networking;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileSystemExample  {
    public static void main(String[] args) throws Exception {
        FileSystem fileSystem = FileSystems.getDefault(); // 파일 시스템 구현 객체를 얻는 정적 메소ㅡ
        for(FileStore store : fileSystem.getFileStores()){
            System.out.println("드라이버명: " + store.name());
            System.out.println("파일시스템: " + store.type());
            System.out.println("전체 공간: " + store.getTotalSpace());
            System.out.println("남은 공간: " + store.getUnallocatedSpace());
            System.out.println("사용 가능공간: " + store.getUsableSpace());
            System.out.println();
        }
        System.out.println("파일 구분자: " + fileSystem.getSeparator());
        for(Path path : fileSystem.getRootDirectories())
            System.out.println(path.toString());
    }
}
