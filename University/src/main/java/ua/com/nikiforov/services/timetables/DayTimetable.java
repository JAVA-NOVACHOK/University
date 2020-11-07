package ua.com.nikiforov.services.timetables;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public class DayTimetable {

    private List<Timetable> timetables;
    private DateInfo dateInfo;

    public DayTimetable() {
        this.dateInfo = new DateInfo();
        this.timetables = new ArrayList<>();
    }

    public DayTimetable(List<Timetable> timetables, DateInfo dateInfo) {
        this.timetables = timetables;
        this.dateInfo = dateInfo;
    }
    
    
    public void addTimetable(Timetable timetable) {
        timetables.add(timetable);
    }

    public List<Timetable> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<Timetable> timetables) {
        this.timetables.addAll(timetables);
    }

    public DateInfo getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(DateInfo dateInfo) {
        this.dateInfo = dateInfo;
    }

}
