package com.ftm.server.common.utils;

import java.security.SecureRandom;

public class RandomCodeCreator {

    private static final int CODE_LENGTH = 6;

    private static final SecureRandom random = new SecureRandom();

    public static String generateAuthCode() {

        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
