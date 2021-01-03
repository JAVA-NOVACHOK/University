package ua.com.nikiforov.dao.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface TeacherTimetableDAO extends JpaRepository<Teacher,Long> {

    @Query("SELECT l from Lesson l WHERE l.teacher = ?1 AND l.lessonDate = ?2 ORDER BY  l.period")
    public List<Lesson> getDayTimetable(Teacher teacher, LocalDate date);

    @Query("SELECT l from Lesson l WHERE l.teacher = ?1 AND l.lessonDate BETWEEN ?2 AND ?3 ORDER BY l.lessonDate, l.period")
    public List<Lesson> getMonthTimetable(Teacher teacher, LocalDate dateFrom, LocalDate dateTo);
}
