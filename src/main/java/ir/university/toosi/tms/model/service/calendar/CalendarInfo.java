package ir.university.toosi.tms.model.service.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CalendarInfo {
    @JsonProperty
    private boolean Status;
    @JsonProperty
    private Year year;

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public static void main(String[] args) {
//        CalendarInfo calendarInfo = new CalendarInfo();
//        calendarInfo.setStatus(true);
//        Year a2013 = new Year();
//        Year.selectBox[] list = new Year.selectBox[4];
//        Year.selectBox a0 = new Year.selectBox();
//        a0.set_id("0");
//        a0.setColor("#ffffff");
//        a0.setTitle("انتخاب");
//        a0.setTimestamp("4523456236-4343456236");
//        Year.selectBox a1 = new Year.selectBox();
//        a1.set_id("1");
//        a1.setColor("#ff000");
//        a1.setTitle("تعطیل");
//        a1.setTimestamp("4523456236-4343456236");
//        Year.selectBox a2 = new Year.selectBox();
//        a2.set_id("2");
//        a2.setColor("#00ff00");
//        a2.setTitle("عادی");
//        a2.setTimestamp("4523456236-4343456236");
//        Year.selectBox a3 = new Year.selectBox();
//        a3.set_id("3");
//        a3.setColor("#0000ff");
//        a3.setTitle("نیمه تعطیل");
//        a3.setTimestamp("4523456236-4343456236");
//        list[0] = a0;
//        list[1] = a1;
//        list[2] = a2;
//        list[3] = a3;
////        list[1]=(a2);
////        list[2]=(a3);
//        a2013.setselectBox(list);
//        Year.Initialize[] listInit = new Year.Initialize[0];
//        a2013.setInitialize(listInit);
//        calendarInfo.year = a2013;
//        try {
//            String s = new ObjectMapper().writeValueAsString(calendarInfo);
//            s = s.replace("year", "2013");
//            System.out.println(s);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }


    public static class Year {
        @JsonProperty
        selectBox[] selectBox;
        @JsonProperty
        Initialize[] Initialize;

        public selectBox[] getselectBox() {
            return selectBox;
        }

        public void setselectBox(selectBox[] selectBox) {
            this.selectBox = selectBox;
        }

        public Year.Initialize[] getInitialize() {
            return Initialize;
        }

        public void setInitialize(Year.Initialize[] initialize) {
            Initialize = initialize;
        }

        public static class selectBox {
            @JsonProperty
            String _id;
            @JsonProperty
            String Color;
            @JsonProperty
            String timestamp;
            @JsonProperty
            String indexday;
            @JsonProperty
            String title;

            public String getIndexday() {
                return indexday;
            }

            public void setIndexday(String indexday) {
                this.indexday = indexday;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getColor() {
                return Color;
            }

            public void setColor(String color) {
                Color = color;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class Initialize {
            @JsonProperty
            String _id;
            @JsonProperty
            String timestamp;
            @JsonProperty
            String day;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }
        }
    }

}
