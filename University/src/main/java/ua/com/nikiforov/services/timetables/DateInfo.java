package ua.com.nikiforov.services.timetables;

public class DateInfo {

    private String weekDay;
    private int monthDay;
    private String month;
    private int year;
    
    public DateInfo() {
    }

    public DateInfo(String weekDay, int monthDay, String month, int year) {
        this.weekDay = weekDay.substring(0, 1) + weekDay.substring(1).toLowerCase();
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

    @Override
    public String toString() {
        return "DateInfo [weekDay=" + weekDay + ", monthDay=" + monthDay + ", month=" + month + ", year=" + year + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((month == null) ? 0 : month.hashCode());
        result = prime * result + monthDay;
        result = prime * result + ((weekDay == null) ? 0 : weekDay.hashCode());
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateInfo other = (DateInfo) obj;
        if (month == null) {
            if (other.month != null)
                return false;
        } else if (!month.equals(other.month))
            return false;
        if (monthDay != other.monthDay)
            return false;
        if (weekDay == null) {
            if (other.weekDay != null)
                return false;
        } else if (!weekDay.equals(other.weekDay))
            return false;
        if (year != other.year)
            return false;
        return true;
    }
    
    

}
