package ua.com.nikiforov.services.timetables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.repositories.timetables.TeacherTimetableRepository;
import ua.com.nikiforov.dto.TimetableDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeachersTimetableService extends PersonalTimetable {

    private static  final Logger LOGGER = LoggerFactory.getLogger(TeachersTimetableService.class);

    private TeacherTimetableRepository teachersTimetable;

    @Autowired
    public TeachersTimetableService(TeacherTimetableRepository teachersTimetable) {
        this.teachersTimetable = teachersTimetable;
    }

    @Override
    @Transactional
    public List<DayTimetable>  getDayTimetable(String date, long teacherId) {
            String timetableInfoMSG = String
                    .format("Teacher's timetable for day by date with such data: %s and teacherId %d", date, teacherId);
            LOGGER.debug("Getting '{}'", timetableInfoMSG);
            List<Lesson> dayTimetable = new ArrayList<>();
            LocalDate time = PersonalTimetable.getLocalDate(date);
            try {
                dayTimetable.addAll(teachersTimetable.getDayTimetable(new Teacher(teacherId),time));
                LOGGER.info("Successfully retrieved {}", timetableInfoMSG);
            } catch (PersistenceException e) {
                String failMessage = "Failed to get " + timetableInfoMSG;
                LOGGER.error(failMessage);
                throw new DataOperationException(failMessage, e);
            }
        List<TimetableDTO> allTimetablesList =
                getTimetableMapper()
                        .getTimetableDTOs(dayTimetable);
        return createMonthTimetable(allTimetablesList);
    }

    @Override
    @Transactional
    public List<DayTimetable> getMonthTimetable(String date, long teacherId) {
        LocalDate timeFrom = PersonalTimetable.getLocalDate(date);
        LocalDate timeTo = timeFrom.plusMonths(1);
        String timetableInfoMSG = String.format(
                "Teacher's timetable for month by date with  teacherId %d and date between %s and %s", teacherId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<Lesson> monthTimetable = new ArrayList<>();
        try {
            monthTimetable.addAll(teachersTimetable.getMonthTimetable(new Teacher(teacherId),timeFrom,timeTo));
            LOGGER.info("Successfully retrieved {}", timetableInfoMSG);
        } catch (PersistenceException e) {
            String failMessage = "Failed to get " + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        List<TimetableDTO> allTimetablesList = getTimetableMapper().getTimetableDTOs(monthTimetable);
        return createMonthTimetable(allTimetablesList);
    }



}
