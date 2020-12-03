package ua.com.nikiforov.services.lesson;

import java.util.List;

import ua.com.nikiforov.controllers.dto.LessonDTO;
import ua.com.nikiforov.models.lesson.Lesson;

public interface LessonService {
    
    public boolean addLesson(LessonDTO lesson);

    public LessonDTO getLessonById(long id);
    
    public LessonDTO getLessonByAllArgs(int period, int subjectId, int roomId, long groupId, String date, long teacherId);

    public List<LessonDTO> getAllLessons();

    public boolean updateLesson(LessonDTO lesson);

    public boolean deleteLessonById(long id);
    

}
