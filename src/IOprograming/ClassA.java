package IOprograming;

import java.io.Serializable;

public class ClassA implements Serializable {
    int filed1;
    ClassB filed2 = new ClassB();
    static int field3;
    transient int filed4;
}
