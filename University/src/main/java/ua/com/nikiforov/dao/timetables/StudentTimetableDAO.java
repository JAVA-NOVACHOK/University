package ua.com.nikiforov.dao.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.lesson.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface StudentTimetableDAO extends JpaRepository<Lesson,Long> {

    @Query("SELECT l FROM Lesson l WHERE l.group = ?1 AND l.lessonDate = ?2 ORDER BY l.period")
    public List<Lesson> getDayTimetable(Group group, LocalDate date);

    @Query("SELECT l FROM Lesson l WHERE l.group = ?1 AND l.lessonDate " +
            "BETWEEN ?2 AND ?3 ORDER BY l.lessonDate, l.period")
    public List<Lesson> getMonthTimetable(Group group, LocalDate dateFrom, LocalDate dateTo);
}
