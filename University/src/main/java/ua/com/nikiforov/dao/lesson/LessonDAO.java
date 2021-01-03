package ua.com.nikiforov.dao.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface LessonDAO extends JpaRepository<Lesson,Long> {

    @Transactional
    public Lesson save(Lesson lesson);

    public Lesson getLessonById(long id);

    @Query("SELECT l FROM Lesson l WHERE l.period = ?1 AND l.subject = ?2 " +
            "AND l.room = ?3  AND l.group = ?4 AND l.lessonDate = ?5 " +
            "AND l.teacher = ?6")
    public Lesson getLessonByAllArgs(int period, Subject subject, Room room, Group group, LocalDate date, Teacher teacher);

    @Query("SELECT l FROM Lesson l ORDER BY l.lessonDate")
    public List<Lesson> getAllLessons();

    @Transactional
    public void deleteLessonById(long id);

}
