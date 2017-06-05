package ir.university.toosi.wtms.web.util;

import java.security.MessageDigest;

public class HashUtil {

    public synchronized static String getSecurePassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static void main(String[] args) {
        System.out.println(HashUtil.getSecurePassword("123"));
    }
}
