package ir.university.toosi.tms.model.entity;


/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public enum GatewayStatus {

    OPEN, CLOSE;

    public String getDescription() {

        switch (this) {

            case OPEN:
                return "Open";
            case CLOSE:
                return "Close";
        }
        return "NONE";
    }

    public String getValue() {
        switch (this) {

            case OPEN:
                return "1";
            case CLOSE:
                return "2";
        }
        return "0";
    }

    public static GatewayStatus findByValue(String value){
        switch (value){
            case "1":
                return OPEN;
            case "2":
                return CLOSE;
        }
        return CLOSE;
    }
}