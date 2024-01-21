package br.com.pnipapi.utils;

import java.util.Base64;

public class Base64Util {

    public static byte[] getBytesFromBase64(String base64) {
        String prefix = "data:application/pdf;base64,";
        if (base64.startsWith(prefix)) {
            base64 = base64.substring(prefix.length());
        }
        return Base64.getDecoder().decode(base64);
    }

}
