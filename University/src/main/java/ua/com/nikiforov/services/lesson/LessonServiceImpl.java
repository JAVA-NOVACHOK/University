package ua.com.nikiforov.services.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.lesson.LessonDAOImpl;
import ua.com.nikiforov.models.Lesson;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAOImpl lessonDAOImpl;

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        return lessonDAOImpl.addLesson(groupId, roomId, subjectId);
    }

    @Override
    public Lesson findLessonById(long id) {
        return lessonDAOImpl.getLessonById(id);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDAOImpl.getAllLessons();
    }

    @Override
    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
        return lessonDAOImpl.updateLesson(groupId, roomId, subjectId, lessonId);
    }

    @Override
    public boolean deleteLessonById(long id) {
        return lessonDAOImpl.deleteLessonById(id);
    }

}
