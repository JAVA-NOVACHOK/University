package ua.com.nikiforov.dao.timetables;

import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

public interface TimetableDAO {

    public boolean addTimetable(long lessonId, long teacherId, String stringDate, Period period);

    public Timetable getTimetableById(long id);
    
    public Timetable getTimetableByLessonTeacherTimePeriod(long lessonId, long teacherId, String stringDate, Period period);

    public List<Timetable> getAllTimetables();

    public boolean updateTimetable(long lessonId, long teacherId, String stringDate, Period period, long id);

    public boolean deleteTimetableById(long id);
    
    public List<Timetable> getDayTimetable(String date,long personId);

    public List<Timetable> getMonthTimetable(String date, long personId);

}
