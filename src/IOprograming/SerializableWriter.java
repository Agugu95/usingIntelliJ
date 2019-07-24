package IOprograming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializableWriter {
    public static void main(String[] args) throws Exception{

        File file = new File("C:/TempT/Object5.dat");
        if(false == (file.exists())) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        ClassA classA = new ClassA();
        classA.filed1 = 1;
        classA.filed2.field1 = 2;
        classA.filed4 = 4;
        ClassA.field3 = 3;
        oos.writeObject(classA);
        oos.flush();
        oos.close();
        fos.close();


    }
}
