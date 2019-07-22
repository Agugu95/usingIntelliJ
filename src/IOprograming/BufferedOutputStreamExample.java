package IOprograming;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BufferedOutputStreamExample {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        int data = -1;
        long start = 0;
        long end = 0;

        fis = new FileInputStream("C:\\Users\\user\\IdeaProjects\\HelloWorld\\src\\IOprograming\\K.jpg");
        System.out.println(fis.read());
        bis = new BufferedInputStream(fis);
        fos = new FileOutputStream("../out1.jpg");
        start = System.currentTimeMillis();
        while ((data = bis.read()) != -1){
            fos.write(data);
        }
        fos.flush();
        end = System.currentTimeMillis();
        fos.close(); bis.close(); fis.close();
        System.out.println("사용하지 않았을 때: " + (end  - start) + "ns");

        fis = new FileInputStream("C:\\Users\\user\\IdeaProjects\\HelloWorld\\src\\IOprograming\\K.jpg");
        bis = new BufferedInputStream(fis);
        fos = new FileOutputStream("../out2.jpg");
        bos = new BufferedOutputStream(fos);
        start = System.currentTimeMillis();
        while ((data = bis.read()) != -1){
            bos.write(data);
        }
        bos.flush();
        end = System.currentTimeMillis();
        bos.close(); fis.close(); bis.close(); fos.close();
        System.out.println("사용 햇을 때: " + (end - start) + "ns");
  }
}
