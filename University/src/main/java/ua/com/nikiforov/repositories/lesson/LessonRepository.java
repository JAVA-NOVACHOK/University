package ua.com.nikiforov.repositories.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;

import java.time.LocalDate;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Query("SELECT l FROM Lesson l WHERE l.period = :period AND l.subject = :subject " +
            "AND l.room = :room  AND l.group = :group AND l.lessonDate = :date " +
            "AND l.teacher = :teacher")
    public Lesson getLessonByAllArgs(
            @Param("period") int period,
            @Param("subject") Subject subject,
            @Param("room") Room room,
            @Param("group") Group group,
            @Param("date") LocalDate date,
            @Param("teacher") Teacher teacher);


}
