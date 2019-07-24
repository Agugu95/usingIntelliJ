package IOprograming;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SerializableReader {
    public static void main(String[] args) throws Exception {
        File file = new File("C:/TempT/Object5.dat");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        ClassA v = (ClassA) ois.readObject();
        System.out.println(v.filed1 + " " + v.filed4 + " " + v.filed2 + " " + v.filed2.field1 + " "+ ClassA.field3);
    }
}
