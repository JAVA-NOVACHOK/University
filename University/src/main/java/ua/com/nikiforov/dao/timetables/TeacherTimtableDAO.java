package ua.com.nikiforov.dao.timetables;

import java.time.Instant;
import java.util.List;

import ua.com.nikiforov.models.timetables.TeachersTimetable;

public interface TeacherTimtableDAO {
    
    public boolean addTeacherTimetable(long lessonId, long teacherId, Instant time);

    public TeachersTimetable getTEACHERTimetableById(long id);

    public List<TeachersTimetable> getAllTEACHERSTimetables();

    public boolean updateTEACHERSTimetable(long lessonId, long teacherId, Instant time, long id);

    public boolean deleteTEACHERSTimetableById(long id);

}
