package ir.university.toosi.wtms.web.util;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class DateConverterUtils {

    public static String getWithSlash(String value) {
        if (value != null && !StringUtils.isEmpty(value) && !value.contains("/") && !value.equalsIgnoreCase("null")) {
            if (value.length() == 12)
                value = value.substring(0, 4) + "/" + value.substring(4, 6) + "/" + value.substring(6, 8) + " " + value.substring(8, 10) + ":" + value.substring(10);
            if (value.length() == 8)
                value = value.substring(0, 4) + "/" + value.substring(4, 6) + "/" + value.substring(6);
            if (value.length() == 6)
                value = value.substring(0, 2) + "/" + value.substring(2, 4) + "/" + value.substring(4, 6);
        }
        return value;
    }
}