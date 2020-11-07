package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

public interface LessonDAO {

    public boolean addLesson(Period period, int subjectId, int roomId, long groupId, String date, long teacherId);

    public Lesson getLessonById(long id);

    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(Period period, int subjectId, int roomId, long groupId, String date, long teacherId,
            long lessonId);

    public boolean deleteLessonById(long id);

    public List<Timetable> getDayStudentTimetable(String date, long studentId);

    public List<Timetable> getMonthStudentTimetable(String stringDate, long studentId);

}
