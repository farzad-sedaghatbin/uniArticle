package ir.university.toosi.tms.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CalendarInfo {

    @JsonProperty
    private Year[] year;

    public Year[] getYear() {
        return year;
    }

    public void setYear(Year[] year) {
        this.year = year;
    }



    public static class Month {
       Day[] day;

        public Day[] getDay() {
            return day;
        }

        public void setDay(Day[] day) {
            this.day = day;
        }
    }   public static class Year {
       Month[] month;

        public Month[] getMonth() {
            return month;
        }

        public void setMonth(Month[] month) {
            this.month = month;
        }
    }
    public static class Day {


        String _id;
        String timestamp;
        String day;
        String indexday;
        String Month;
        String Year;

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

        public String getMonth() {
            return Month;
        }

        public void setMonth(String month) {
            Month = month;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String year) {
            Year = year;
        }


    }

}
