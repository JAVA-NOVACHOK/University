package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.controllers.dto.LessonDTO;
import ua.com.nikiforov.models.lesson.Lesson;

public interface LessonDAO {

    public boolean addLesson(LessonDTO lesson);

    public Lesson getLessonById(long id);

    public Lesson getLessonByAllArgs(int period, int subjectId, int roomId, long groupId, String date, long teacherId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(LessonDTO lesson);

    public boolean deleteLessonById(long id);

}
