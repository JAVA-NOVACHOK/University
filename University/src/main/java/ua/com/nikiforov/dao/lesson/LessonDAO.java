<<<<<<< HEAD
package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.Lesson;

public interface LessonDAO {
    
    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson getLessonById(long id);
    
    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);

}
=======
package ua.com.nikiforov.dao.lesson;

import java.util.List;

import ua.com.nikiforov.models.Lesson;

public interface LessonDAO {
    
    public boolean addLesson(long groupId, int roomId, int subjectId);

    public Lesson getLessonById(long id);
    
    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId);

    public List<Lesson> getAllLessons();

    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId);

    public boolean deleteLessonById(long id);

}
>>>>>>> refs/remotes/origin/master
