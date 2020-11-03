package ua.com.nikiforov.services.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.lesson.LessonInfo;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        return lessonDAO.addLesson(groupId, roomId, subjectId);
    }

    @Override
    public Lesson getLessonById(long id) {
        return lessonDAO.getLessonById(id);
    }
    
    @Override
    public LessonInfo getLessonInfoById(long lessonId) {
        Lesson lesson = getLessonById(lessonId);
        return lessonDAO.getLessonInfoById(lesson);
    }

    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId) {
        return lessonDAO.getLessonByGroupRoomSubjectIds(groupId, roomId, subjectId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDAO.getAllLessons();
    }

    @Override
    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
        return lessonDAO.updateLesson(groupId, roomId, subjectId, lessonId);
    }

    @Override
    public boolean deleteLessonById(long id) {
        return lessonDAO.deleteLessonById(id);
    }

}
