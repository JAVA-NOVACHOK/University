package ua.com.nikiforov.dao.timetables;

import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public interface TeachersTimetableDAO {
    
    public List<Timetable> getDayTeacherTimetable(String date, long teacherId);

    public List<Timetable> getMonthTeacherTimetable(String date, long teacherId);
}
