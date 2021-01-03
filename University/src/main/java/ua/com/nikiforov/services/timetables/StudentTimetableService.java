package ua.com.nikiforov.services.timetables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.timetables.StudentTimetableDAO;
import ua.com.nikiforov.dto.TimetableDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.lesson.Lesson;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentTimetableService extends PersonalTimetable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentTimetableService.class);

    private static final String FAILED_MSG = "Failed to get ";
    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIEVED_MSG = "Successfully retrieved {}";

    private StudentTimetableDAO studentsTimetable;

    @Autowired
    public StudentTimetableService(StudentTimetableDAO studentsTimetable) {
        this.studentsTimetable = studentsTimetable;
    }


    @Override
    @Transactional
    public List<DayTimetable> getDayTimetable(String date, long groupId) {
        String timetableInfoMSG = String.format("Student's timetable for day by date with such data: %s and groupId %d",
                date, groupId);
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<Lesson> dayTimetable = new ArrayList<>();
        LocalDate time = PersonalTimetable.getLocalDate(date);
        try {
            dayTimetable.addAll(studentsTimetable.getDayTimetable(new Group(groupId),time));
            LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        List<TimetableDTO> allTimetablesList = getTimetableMapper().getTimetableDTOs(dayTimetable);
        return createMonthTimetable(allTimetablesList);
    }

    @Override
    @Transactional
    public List<DayTimetable> getMonthTimetable(String date, long groupId) {
        LocalDate timeFrom = PersonalTimetable.getLocalDate(date);
        LocalDate timeTo = timeFrom.plusMonths(1);
        String timetableInfoMSG = String.format(
                "Student's timetable for month by date with  groupId %d and date between %s and %s", groupId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<Lesson> monthTimetable = new ArrayList<>();
        try {
            monthTimetable.addAll(studentsTimetable.getMonthTimetable(new Group(groupId),timeFrom,timeTo));
            LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        List<TimetableDTO> allTimetablesList = getTimetableMapper().getTimetableDTOs(monthTimetable);
        return createMonthTimetable(allTimetablesList);
    }


}
