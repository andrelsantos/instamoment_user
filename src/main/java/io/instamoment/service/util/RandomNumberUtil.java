package io.instamoment.service.util;

import java.util.Random;

public class RandomNumberUtil {
    public static int generateRandomNumberWithFiveDigits() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
    }
}
