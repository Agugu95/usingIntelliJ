package IOprograming;

import java.io.*;

public class NonSerializableExample {
    public static void main(String[] args) throws Exception {
        File file = new File("C:/TempT/Object6.dat");
        if(false == file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Child child = new Child();
        child.field1 = "홍길동";
        child.field2 = "홍삼워";
        oos.writeObject(child);
        oos.flush(); oos.close(); fos.close();

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Child v = (Child) ois.readObject();
        System.out.println(v.field1 + " " + v.field2);
        ois.close(); fis.close();
    }
}
