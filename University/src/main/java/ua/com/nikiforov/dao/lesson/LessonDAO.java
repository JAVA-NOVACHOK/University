package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.models.lesson.Lesson;

public interface LessonDAO {

    public void addLesson(Lesson lesson);

    public Lesson getLessonById(long id);

    public Lesson getLessonByAllArgs(Lesson lesson);

    public List<Lesson> getAllLessons();

    public void updateLesson(Lesson lesson);

    public void deleteLessonById(long id);

}
