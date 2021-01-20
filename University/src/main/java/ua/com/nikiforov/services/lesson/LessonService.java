package ua.com.nikiforov.services.lesson;

import java.util.List;

import ua.com.nikiforov.dto.LessonDTO;

public interface LessonService {
    
    public LessonDTO addLesson(LessonDTO lesson);

    public LessonDTO getLessonById(long id);
    
    public LessonDTO getLessonByAllArgs(LessonDTO lessonDTO);

    public List<LessonDTO> getAllLessons();

    public LessonDTO updateLesson(LessonDTO lesson);

    public void deleteLessonById(long id);
    

}
