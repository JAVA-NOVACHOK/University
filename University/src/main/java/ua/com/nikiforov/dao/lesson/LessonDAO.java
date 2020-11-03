package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.lesson.LessonInfo;

public interface LessonDAO {

    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson getLessonById(long id);

    public LessonInfo getLessonInfoById(Lesson lesson);

    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);

}
