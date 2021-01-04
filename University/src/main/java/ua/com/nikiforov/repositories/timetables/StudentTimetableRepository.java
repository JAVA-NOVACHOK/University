package ua.com.nikiforov.repositories.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.lesson.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface StudentTimetableRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.group = :group AND l.lessonDate = :date ORDER BY l.period")
    public List<Lesson> getDayTimetable(
            @Param("group") Group group,
            @Param("date") LocalDate date);

    @Query("SELECT l FROM Lesson l WHERE l.group = :group AND l.lessonDate " +
            "BETWEEN :dateFrom AND :dateTo ORDER BY l.lessonDate, l.period")
    public List<Lesson> getMonthTimetable(
            @Param("group") Group group,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);
}
