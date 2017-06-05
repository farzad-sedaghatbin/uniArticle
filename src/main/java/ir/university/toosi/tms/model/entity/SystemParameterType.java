package ir.university.toosi.tms.model.entity;


/**
 * @author : Hamed Hatami , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public enum SystemParameterType {

    YEAR, USER_EXTRA_FIELD_1, USER_EXTRA_FIELD_2, USER_EXTRA_FIELD_3, USER_EXTRA_FIELD_4, PERSON_EXTRA_FIELD_1, PERSON_EXTRA_FIELD_2, PERSON_EXTRA_FIELD_3, PERSON_EXTRA_FIELD_4, LOGIN_COUNT, SENTRY_COUNT, TRAFFIC_LOG_COMMENT_VALID_TIME, PERSON_NAME, PERSON_LASTNAME, PERSON_NO;

    public String getDescription() {

        switch (this) {

            case YEAR:
                return "Year";
            case USER_EXTRA_FIELD_1:
                return "USER_EXTRA_FIELD_1";
            case USER_EXTRA_FIELD_2:
                return "USER_EXTRA_FIELD_2";
            case USER_EXTRA_FIELD_3:
                return "USER_EXTRA_FIELD_3";
            case USER_EXTRA_FIELD_4:
                return "USER_EXTRA_FIELD_4";
            case PERSON_EXTRA_FIELD_1:
                return "PERSON_EXTRA_FIELD_1";
            case PERSON_EXTRA_FIELD_2:
                return "PERSON_EXTRA_FIELD_2";
            case PERSON_EXTRA_FIELD_3:
                return "PERSON_EXTRA_FIELD_3";
            case PERSON_EXTRA_FIELD_4:
                return "PERSON_EXTRA_FIELD_4";
            case LOGIN_COUNT:
                return "LOGIN_COUNT";
            case SENTRY_COUNT:
                return "SENTRY_COUNT";
            case PERSON_NAME:
                return "firstname";
            case PERSON_LASTNAME:
                return "lastname";
            case PERSON_NO:
                return "personnelCode";
            case TRAFFIC_LOG_COMMENT_VALID_TIME:
                return "TRAFFIC_LOG_COMMENT_VALID_TIME";
        }


        return "NONE";
    }

    public String getValue() {
        switch (this) {

            case YEAR:
                return "1";

        }
        return "0";
    }
}