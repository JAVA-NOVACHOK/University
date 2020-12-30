package ua.com.nikiforov.dao.timetables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

@Repository
@Qualifier("teachersTimetable")
public class TeachersTimetableDAOImpl implements TimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersTimetableDAOImpl.class);

    private static final String GET_TEACHER_DAY_TIMETABLE = "SELECT l from Lesson l WHERE l.teacher = ?1 AND l.lessonDate = ?2 ORDER BY  l.period";
    private static final String GET_TEACHER_MONTH_TIMETABLE = "SELECT l from Lesson l WHERE l.teacher = ?1 AND l.lessonDate BETWEEN ?2 AND ?3 ORDER BY l.lessonDate, l.period";

    private static final int FIRST_PARAMETER_INDEX = 1;
    private static final int SECOND_PARAMETER_INDEX = 2;
    private static final int THIRD_PARAMETER_INDEX = 3;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Lesson> getDayTimetable(String date, long teacherId) {
        String timetableInfoMSG = String
                .format("Teacher's timetable for day by date with such data: %s and teacherId %d", date, teacherId);
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<Lesson> dayTimetable = new ArrayList<>();
        Teacher teacher = entityManager.getReference(Teacher.class, teacherId);
        LocalDate time = PersonalTimetable.getLocalDate(date);
        try {
            dayTimetable.addAll(
                    entityManager.createQuery(GET_TEACHER_DAY_TIMETABLE)
                    .setParameter(FIRST_PARAMETER_INDEX,teacher)
                    .setParameter(SECOND_PARAMETER_INDEX,time)
                    .getResultList());
            LOGGER.info("Successfully retrieved {}", timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = "Failed to get " + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return dayTimetable;
    }

    @Override
    public List<Lesson> getMonthTimetable(String date, long teacherId) {
        LocalDate timeFrom = PersonalTimetable.getLocalDate(date);
        LocalDate    timeTo = timeFrom.plusMonths(1);
        String timetableInfoMSG = String.format(
                "Teacher's timetable for month by date with  teacherId %d and date between %s and %s", teacherId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        Teacher teacher = entityManager.getReference(Teacher.class, teacherId);
        List<Lesson> dayLessons = new ArrayList<>();
        try {
            dayLessons.addAll(entityManager.createQuery(GET_TEACHER_MONTH_TIMETABLE)
                    .setParameter(FIRST_PARAMETER_INDEX,teacher)
                    .setParameter(SECOND_PARAMETER_INDEX,timeFrom)
                    .setParameter(THIRD_PARAMETER_INDEX,timeTo)
                    .getResultList());
            LOGGER.info("Successfully retrieved {}", timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = "Failed to get " + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return dayLessons;
    }
}
