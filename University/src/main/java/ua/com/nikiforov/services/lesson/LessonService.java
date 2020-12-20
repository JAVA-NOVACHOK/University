package ua.com.nikiforov.services.lesson;

import java.util.List;

import ua.com.nikiforov.dto.LessonDTO;

public interface LessonService {
    
    public void addLesson(LessonDTO lesson);

    public LessonDTO getLessonById(long id);
    
    public LessonDTO getLessonByAllArgs(LessonDTO lessonDTO);
//    public LessonDTO getLessonByAllArgs(int period, int subjectId, int roomId, long groupId, String date, long teacherId);

    public List<LessonDTO> getAllLessons();

    public void updateLesson(LessonDTO lesson);

    public void deleteLessonById(long id);
    

}
