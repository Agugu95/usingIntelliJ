package Networking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncFileChannelExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 10; i++) {
            Path path = Paths.get("C:/TempT/file" + i + ".txt");
            Files.createDirectories(path.getParent());

            // 비동기 파일 채널 생성
            AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(
                    path,
                    EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE),
                    executorService
            );

            // 파일에 저장할 데이터를 ByteBuffer에 저장
            Charset charset = Charset.defaultCharset();
            ByteBuffer byteBuffer = charset.encode("허거거거거ㅓ걱ㄱ!!!!!!!!");

            // 첨부 객체 생성
            class Attachment {
                Path path;
                AsynchronousFileChannel fileChannel;
            }
            Attachment attachment = new Attachment();
            attachment.path = path;
            attachment.fileChannel = asyncChannel;

            // CompletionHandler 객체 생성
            CompletionHandler<Integer, Attachment> handler = new CompletionHandler<Integer, Attachment>() {
                @Override
                public void completed(Integer result, Attachment attachment) {
                    System.out.println(attachment.path.getFileName() + " : " +
                            result + "byte 썼음, 스레드 : " + Thread.currentThread().getName());
                }

                @Override
                public void failed(Throwable exc, Attachment attachment) {
                    exc.printStackTrace();
                    try {
                        attachment.fileChannel.close();
                    } catch (IOException e) {

                    }
                }
            };
            asyncChannel.write(byteBuffer, 0, attachment, handler);
        }
            // 스레드 풀 종료
        Thread.sleep(1000);
        executorService.shutdown();
    }

}