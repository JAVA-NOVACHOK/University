package ua.com.nikiforov.services.lesson;

import java.util.List;

import ua.com.nikiforov.models.Lesson;

public interface LessonService {
    
    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson findLessonById(long id);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);

}
