package ir.university.toosi.tms.model.entity;

/**
 * Created by O_Javaheri on 11/17/2015.
 */
public class GatewayStatusObject {

    private String name;
    private String value;

    public GatewayStatusObject(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
