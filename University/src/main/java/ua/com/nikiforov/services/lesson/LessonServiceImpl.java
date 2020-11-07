package ua.com.nikiforov.services.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.services.timetables.Period;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public boolean addLesson(Period period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        return lessonDAO.addLesson(period, subjectId, roomId, groupId, date, teacherId);
    }

    @Override
    public Lesson getLessonById(long id) {
        return lessonDAO.getLessonById(id);
    }

    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId) {
        return lessonDAO.getLessonByGroupRoomSubjectIds(groupId, roomId, subjectId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDAO.getAllLessons();
    }

    @Override
    public boolean updateLesson(Period period, int subjectId, int roomId, long groupId, String date, long teacherId,
            long lessonId) {
        return lessonDAO.updateLesson(period, subjectId, roomId, groupId, date, teacherId,
                lessonId);
    }

    @Override
    public boolean deleteLessonById(long id) {
        return lessonDAO.deleteLessonById(id);
    }

   
}
