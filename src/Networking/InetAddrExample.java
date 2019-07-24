package Networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddrExample {
    public static void main(String[] args)  {
        try {
            InetAddress local  = InetAddress.getLocalHost();
            System.out.println("local host: " + local);

            InetAddress[] iarr = InetAddress.getAllByName("www.naver.com");
            for(InetAddress remote : iarr){
                System.out.println("naver: " + remote.getHostAddress());
            }

        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
}
