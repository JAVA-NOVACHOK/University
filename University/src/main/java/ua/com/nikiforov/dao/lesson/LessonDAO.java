package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.Lesson;

public interface LessonDAO {
    
    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson findLessonById(long id);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);

}
