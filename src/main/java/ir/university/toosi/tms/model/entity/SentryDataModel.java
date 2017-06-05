package ir.university.toosi.tms.model.entity;


public class SentryDataModel extends BaseEntity {

    private long id;
    private long personId;
    private String name;
    private String pdpName;
    private String gate;
    private String traffic_time;
    private String traffic_date;
    private String video ;
    private boolean valid;
    private boolean exit;



    public SentryDataModel() {
    }


    public SentryDataModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTime() {
        return traffic_time;
    }

    public void setTime(String time) {
        this.traffic_time = time;
    }

    public String getDate() {
        return traffic_date.replace("/","");
    }

    public void setDate(String date) {
        this.traffic_date = date;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTraffic_time() {
        return traffic_time;
    }

    public void setTraffic_time(String traffic_time) {
        this.traffic_time = traffic_time;
    }

    public String getTraffic_date() {
        return traffic_date;
    }

    public void setTraffic_date(String traffic_date) {
        this.traffic_date = traffic_date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getPdpName() {
        return pdpName;
    }

    public void setPdpName(String pdpName) {
        this.pdpName = pdpName;
    }
}
