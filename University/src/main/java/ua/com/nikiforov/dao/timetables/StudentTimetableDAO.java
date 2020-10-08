package ua.com.nikiforov.dao.timetables;

import java.time.Instant;
import java.util.List;

import ua.com.nikiforov.models.timetables.StudentsTimetable;

public interface StudentTimetableDAO {

    public boolean addStudentsTimetable(long lessonId, long studentId, Instant time);

    public StudentsTimetable getStudentTimetableById(long id);

    public List<StudentsTimetable> getAllStudentsTimetables();

    public boolean updateStudentsTimetable(long lessonId, long studentId, Instant time, long id);

    public boolean deleteStudentsTimetableById(long id);

}
