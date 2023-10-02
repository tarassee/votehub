package com.tarasiuk.votehub.util;

public final class GammaUtil {

    private static final String KEYBOARD_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 `~!@#$%^&*()-=_+[]\\{}|;':\",./<>?";
    private static final String GAMMA = "XMCKL";

    private GammaUtil() {
    }

    public static String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int textCharIndex = KEYBOARD_SYMBOLS.indexOf(text.charAt(i));
            int gammaCharIndex = KEYBOARD_SYMBOLS.indexOf(GAMMA.charAt(i % GAMMA.length()));
            int encryptedCharIndex = (textCharIndex + gammaCharIndex) % KEYBOARD_SYMBOLS.length();
            encryptedText.append(KEYBOARD_SYMBOLS.charAt(encryptedCharIndex));
        }
        return encryptedText.toString();
    }

    public static String decrypt(String encryptedText) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < encryptedText.length(); i++) {
            int encryptedCharIndex = KEYBOARD_SYMBOLS.indexOf(encryptedText.charAt(i));
            int gammaCharIndex = KEYBOARD_SYMBOLS.indexOf(GAMMA.charAt(i % GAMMA.length()));
            int textCharIndex = (encryptedCharIndex - gammaCharIndex + KEYBOARD_SYMBOLS.length()) % KEYBOARD_SYMBOLS.length();
            decryptedText.append(KEYBOARD_SYMBOLS.charAt(textCharIndex));
        }
        return decryptedText.toString();
    }

}
