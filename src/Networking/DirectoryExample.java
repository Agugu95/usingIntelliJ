package Networking;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryExample {
    public static void main(String[] args) throws Exception{
        Path path1 = Paths.get("C:TempT/dir/subdir");
        Path path2 = Paths.get("C:TempT/file.txt");
        if (Files.notExists(path1)){
            Files.createDirectories(path1);
        }
        if (Files.notExists(path2))
        {
            Files.createFile(path2);
        }

        Path path3 = Paths.get("C:/TempT");
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path3);
        for(Path path: directoryStream){
            if (Files.notExists(path)) {
                System.out.println("디렉토리: " + path.getFileName());
            } else {
                System.out.println("파일: " + path.getFileName() + Files.size(path));
            }
        }

    }
}
