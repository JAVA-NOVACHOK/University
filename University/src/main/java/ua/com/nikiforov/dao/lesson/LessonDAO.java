package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.models.lesson.Lesson;

public interface LessonDAO {

    public void addLesson(LessonDTO lesson);

    public Lesson getLessonById(long id);

    public Lesson getLessonByAllArgs(LessonDTO lesson);

    public List<Lesson> getAllLessons();

    public void updateLesson(LessonDTO lesson);

    public void deleteLessonById(long id);

}
