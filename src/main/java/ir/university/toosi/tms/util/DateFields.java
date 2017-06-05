package ir.university.toosi.tms.util;

public class DateFields {
    public int year;
    public int month;
    public int day;

    public DateFields(int year, int month, int day) {
        super();
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public DateFields() {
        this(0, 0, 0);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public String toString() {
        return "" + year + "/" + (month + 1) + "/" + day;
    }
}