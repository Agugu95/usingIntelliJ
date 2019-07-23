package IOprograming;

import java.io.*;

public class ObjectIOStreamExample {
    public static void main(String[] args) throws  Exception{
        File file = new File(("C:/TempT/Object.dat"));
        boolean isExist = file.exists();
        if (isExist == false){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutput os = new ObjectOutputStream(fos);

        os.writeObject(new Integer(10));
        os.writeObject(new Double(3.14));
        os.writeObject(new int[] {1,24,5});
        os.writeObject(new String("누네띠네"));
        os.flush(); os.close(); fos.close();

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Integer obj1 = (Integer) ois.readObject();
        Double obj2 = (Double) ois.readObject();
        int[] obj3 = (int[]) ois.readObject();
        String obj4 = (String) ois.readObject();
        System.out.println(obj3[1]);
        System.out.println(obj1 + " " + obj2 + " "  + " " + obj4);
    }
}
