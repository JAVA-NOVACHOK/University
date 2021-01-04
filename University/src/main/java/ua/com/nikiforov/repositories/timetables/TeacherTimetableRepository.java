package ua.com.nikiforov.repositories.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface TeacherTimetableRepository extends JpaRepository<Teacher,Long> {

    @Query("SELECT l from Lesson l WHERE l.teacher = :teacher AND l.lessonDate = :date ORDER BY  l.period")
    public List<Lesson> getDayTimetable(
            @Param("teacher") Teacher teacher,
            @Param("date") LocalDate date);

    @Query("SELECT l from Lesson l WHERE l.teacher = :teacher AND l.lessonDate " +
            "BETWEEN :dateFrom AND :dateTo ORDER BY l.lessonDate, l.period")
    public List<Lesson> getMonthTimetable(
            @Param("teacher") Teacher teacher,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);
}
