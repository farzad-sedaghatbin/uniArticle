package ir.university.toosi.tms.model.entity;


/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public enum EventLogType {

    ADD, EDIT, DELETE, SEARCH, TRAFFIC;

    public String getDescription() {

        switch (this) {

            case ADD:
                return "Add";
            case EDIT:
                return "Edit";
            case DELETE:
                return "Delete";
            case SEARCH:
                return "Search";
            case TRAFFIC:
                return "Traffic";
        }
        return "NONE";
    }

    public String getValue() {
        switch (this) {

            case ADD:
                return "1";
            case EDIT:
                return "2";
            case DELETE:
                return "3";
            case SEARCH:
                return "4";
            case TRAFFIC:
                return "5";
        }
        return "0";
    }
}