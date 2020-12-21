package ua.com.nikiforov.dao.timetables;

import java.util.List;

import ua.com.nikiforov.models.lesson.Lesson;

public interface TimetableDAO {
    
    public List<Lesson> getDayTimetable(String date, long id);

    public List<Lesson> getMonthTimetable(String date, long id);

}
