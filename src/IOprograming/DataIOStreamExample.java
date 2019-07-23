package IOprograming;

import java.io.*;

public class DataIOStreamExample {
    public static void main(String[] args) throws  Exception{
        File file = new File("C:/TempT/primitive.dat");
        boolean isExist = file.exists();
        if (isExist == false){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF("누네띠네");
        dos.writeDouble(95.5);
        dos.writeInt(1);

        dos.writeUTF("스팸바");
        dos.writeDouble(90.3);
        dos.writeInt(2);

        dos.flush(); dos.close(); fos.close();

        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        for (int i =0; i<2; i++){
            String name = dis.readUTF();
            int score = dis.readInt();
            int order = dis.readInt();
            System.out.println(name +" " + score + " " +order);
        }
        dis.close(); fis.close();
    }
}
