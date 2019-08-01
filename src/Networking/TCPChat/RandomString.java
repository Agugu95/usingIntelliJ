package Networking.TCPChat;

import java.util.Random;

public class RandomString {
    public static final String NUMBER = "0123456789";
    public static final String ALPABET_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPABET_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPABET = ALPABET_LOWER_CASE+ ALPABET_UPPER_CASE;

    public static String random(int length) throws Exception {

        char[] chars = ALPABET.toCharArray();
        if (length < 1) {
            throw new Exception();
        }
        StringBuffer rand = new StringBuffer(length);
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < length; i++) {
            rand.append(random.nextInt(NUMBER.length()));
            rand.append(chars[random.nextInt(chars.length)]);
            if (rand.length() > 10) {
                break;
            }
        }
        return rand.toString();
    }
}
