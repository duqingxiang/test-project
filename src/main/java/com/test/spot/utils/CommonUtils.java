package com.test.spot.utils;

public class CommonUtils {
    private static String KEY_TEMPLATE = "%s_%d";

    public static String generateCommonKey(String tag, Long transactionId) {
        return String.format(KEY_TEMPLATE, tag, transactionId);
    }
}
