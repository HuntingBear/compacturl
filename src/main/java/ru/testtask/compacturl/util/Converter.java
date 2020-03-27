package ru.testtask.compacturl.util;

class Converter {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    static String convert(long i) {
        StringBuilder sb = new StringBuilder();
        if (i == 0) {
            return "0";
        }
        while (i > 0) {
            int rem = (int) (i % BASE);
            sb.append(ALPHABET.charAt(rem));
            i = i / BASE;
        }
        return sb.toString();
    }

    static long restore(String str) {
        long n = 0;
        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            n += ALPHABET.indexOf(chars[i]) * (int) Math.pow(BASE, i);
        }
        return n;
    }

}

