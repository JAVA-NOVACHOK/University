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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateInfo == null) ? 0 : dateInfo.hashCode());
        result = prime * result + ((timetables == null) ? 0 : timetables.hashCode());
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
        DayTimetable other = (DayTimetable) obj;
        if (dateInfo == null) {
            if (other.dateInfo != null)
                return false;
        } else if (!dateInfo.equals(other.dateInfo))
            return false;
        if (timetables == null) {
            if (other.timetables != null)
                return false;
        } else if (!timetables.equals(other.timetables))
            return false;
        return true;
    }
    
    

}
