package ua.com.nikiforov.services.lesson;

import java.util.List;

import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.lesson.LessonInfo;
import ua.com.nikiforov.models.timetable.DateInfo;
import ua.com.nikiforov.models.timetable.Timetable;

public interface LessonService {
    
    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson getLessonById(long id);
    
    public LessonInfo getLessonInfoById(long lessonId);
    
    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);
    
    public DateInfo parseInstantToDateInfo(Timetable timetable, String zone);

}
