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
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

@Repository
@Qualifier("studentTimetable")
public class StudentTimetableDAO implements TimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentTimetableDAO.class);

    private static final String GET_STUDENT_DAY_TIMETABLE = "SELECT l FROM Lesson l WHERE l.group = ?1 AND l.lessonDate = ?2 ORDER BY l.period";


    private static final String GET_STUDENT_MONTH_TIMETABLE = "SELECT l FROM Lesson l WHERE l.group = ?1 AND l.lessonDate BETWEEN ?2 AND ?3 ORDER BY l.lessonDate, l.period";

    private static final String FAILED_MSG = "Failed to get ";
    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIEVED_MSG = "Successfully retrived {}";

    private static final int FIRST_PARAMETER_INDEX = 1;
    private static final int SECOND_PARAMETER_INDEX = 2;
    private static final int THIRD_PARAMETER_INDEX = 3;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Lesson> getDayTimetable(String date, long groupId) {
        String timetableInfoMSG = String.format("Student's timetable for day by date with such data: %s and groupId %d",
                date, groupId);
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<Lesson> dayTimetable = new ArrayList<>();
        Group group = entityManager.getReference(Group.class,groupId);
        LocalDate time = PersonalTimetable.getLocalDate(date);
        try {
            dayTimetable.addAll(
                    entityManager.createQuery(GET_STUDENT_DAY_TIMETABLE)
                            .setParameter(FIRST_PARAMETER_INDEX,group)
                            .setParameter(SECOND_PARAMETER_INDEX,time)
                            .getResultList());
            LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return dayTimetable;
    }

    @Override
    public List<Lesson> getMonthTimetable(String date, long groupId) {
        LocalDate timeFrom = PersonalTimetable.getLocalDate(date);
        LocalDate timeTo = timeFrom.plusMonths(1);
        Group group = entityManager.getReference(Group.class,groupId);
        String timetableInfoMSG = String.format(
                "Student's timetable for month by date with  groupId %d and date between %s and %s", groupId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<Lesson> lessons = new ArrayList<>();
        try {
            lessons.addAll(
                    entityManager.createQuery(GET_STUDENT_MONTH_TIMETABLE)
                            .setParameter(FIRST_PARAMETER_INDEX,group)
                            .setParameter(SECOND_PARAMETER_INDEX,timeFrom)
                            .setParameter(THIRD_PARAMETER_INDEX,timeTo)
                            .getResultList());
            LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return lessons;
    }


}
