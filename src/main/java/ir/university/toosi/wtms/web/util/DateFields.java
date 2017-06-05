package ir.university.toosi.wtms.web.util;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class DateFields {
    // year
    /**
     * This field denotes the Persian year.
     */
    public int year;

    /**
     * Accessor method to assign a new value to year.
     *
     * @param year The new value to be assigned to year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Accessor method to fetch the value of year.
     *
     * @return The value of year.
     */
    public int getYear() {
        return year;
    }

    // month
    /**
     * This field denotes the Persian month.
     * <p>Note: month is zero-based.
     * See constants in <code>SimplePersianCalendar</code>.
     */
    public int month;

    /**
     * Accessor method to assign a new value to month.
     *
     * @param month The new value to be assigned to month.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Accessor method to fetch the value of month.
     *
     * @return The value of month.
     */
    public int getMonth() {
        return month;
    }

    // day
    /**
     * This field denotes the Persian day.
     */
    public int day;

    /**
     * Accessor method to assign a new value to day.
     *
     * @param day The new value to be assigned to day.
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Accessor method to fetch the value of day.
     *
     * @return The value of day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Constructs a <code>DateFields</code> object with the given date fields.
     */
    public DateFields(int year, int month, int day) {
        super();
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * Constructs a <code>DateFields</code> object with the date fields initialized to 0.
     */
    public DateFields() {
        this(0, 0, 0);
    }

    /**
     * This method returns a usable string representation of this object.
     * Month is incremented to show one-based Persian month index.
     *
     * @return A usable string representation of this object.
     */

    public String toString() {
        return "" + year + "/" + (month + 1) + "/" + day;
    }
}