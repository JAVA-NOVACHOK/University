package ua.com.nikiforov.dao.timetables;

import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public interface TimetableDAO {
    
    public List<Timetable> getDayTimetable(String date, long id);

    public List<Timetable> getMonthTimetable(String date, long id);
}
