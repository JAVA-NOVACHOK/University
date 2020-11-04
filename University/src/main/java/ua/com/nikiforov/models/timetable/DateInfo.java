package ua.com.nikiforov.models.timetable;

public class DateInfo {

    private String weekDay;
    private int monthDay;
    private String month;
    private int year;

    public DateInfo(String weekDay, int monthDay, String month, int year) {
        this.weekDay = weekDay;
        this.monthDay = monthDay;
        this.month = month;
        this.year = year;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(int monthDay) {
        this.monthDay = monthDay;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
