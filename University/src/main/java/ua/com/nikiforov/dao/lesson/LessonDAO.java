package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.services.timetables.Period;

public interface LessonDAO {

    public boolean addLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId);

    public Lesson getLessonById(long id);

    public Lesson getLessonByGroupRoomSubjectIds(int subjectId, int roomId, long groupId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId,
            long lessonId);

    public boolean deleteLessonById(long id);

}
