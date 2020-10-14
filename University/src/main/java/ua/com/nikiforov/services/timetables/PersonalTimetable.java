package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public interface PersonalTimetable {
    
    public boolean addTimetable(long lessonId, long teacherId, String time, Period period);

    public Timetable getTimetableById(long id);

    public List<Timetable> getAllTimetables();

    public boolean updateTimetable(long lessonId, long teacherId, String date, Period period, long id);

    public boolean deleteTimetableById(long id);
    
    public List<Timetable> getDayTimetable();
    
    public List<Timetable> getMonthTimetable();
    

}
