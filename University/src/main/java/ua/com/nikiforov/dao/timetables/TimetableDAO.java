package ua.com.nikiforov.dao.timetables;

import java.time.Instant;
import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public interface TimetableDAO {
    
    public boolean addTimetable(long lessonId, long teacherId, Instant time);

    public Timetable getTimetableById(long id);

    public List<Timetable> getAllTimetables();

    public boolean updateTimetable(long lessonId, long teacherId, Instant time, long id);

    public boolean deleteTimetableById(long id);

}
