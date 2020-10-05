package ua.com.nikiforov.models.timetables;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.services.TimetableService;

public abstract class Timetable {

    protected TimetableService timetableService;
    protected List<Lesson> dayTimetable;
    protected List<Lesson> monthTimetable;

    public Timetable() {
        dayTimetable = new ArrayList<>();
        monthTimetable = new ArrayList<>();
    }

    public List<Lesson> getDayTimetable() {
        setDayTimtable();
        return dayTimetable;
    }

    public List<Lesson> getMonthTimetable() {
        setMonthTimetable();
        return monthTimetable;
    }

    public abstract void setDayTimtable();

    public abstract void setMonthTimetable();

}
