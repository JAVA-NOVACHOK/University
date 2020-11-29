package ua.com.nikiforov.dao.timetables;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.controllers.dto.TimetableDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers.timetables.TimetableMapper;

@Repository
@Qualifier("teachersTimetable")
public class TeachersTimetableDAOImpl implements TimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersTimetableDAOImpl.class);

    private static final String GET_TEACHER_DAY_TIMETABLE = "SELECT period, subject_name, "
            + "room_number, group_name, time, first_name, last_name,lesson_id,teachers.teacher_id FROM lessons "
            + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.teacher_id = ? AND time = ?";

    private static final String GET_TEACHER_MONTH_TIMETABLE = "SELECT period, subject_name, "
            + "room_number, group_name, time, first_name, last_name,lesson_id,teachers.teacher_id FROM lessons "
            + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.teacher_id = ? AND time BETWEEN ? AND ?";

    private JdbcTemplate jdbcTemplate;
    private TimetableMapper timetableMapper;

    @Autowired
    public TeachersTimetableDAOImpl(DataSource dataSource, TimetableMapper timetableMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public List<TimetableDTO> getDayTimetable(String date, long teacherId) {
        String timetableInfoMSG = String
                .format("Teacher's timetable for day by date with such data: %s and teacherId %d", date, teacherId);
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<TimetableDTO> dayTimetable = new ArrayList<>();
        Timestamp time = getTimestampFromString(date);
        try {
            dayTimetable.addAll(
                    jdbcTemplate.query(GET_TEACHER_DAY_TIMETABLE, new Object[] { teacherId, time }, timetableMapper));
            LOGGER.info("Successfully retrived {}", timetableInfoMSG);
        } catch (DataAccessException e) {
            String failMessage = "Failed to get " + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return dayTimetable;
    }

    @Override
    public List<TimetableDTO> getMonthTimetable(String date, long teacherId) {
        Timestamp timeFrom = getTimestampFromString(date);
        LocalDate localDate = timeFrom.toLocalDateTime().toLocalDate();
        Timestamp timeTo = Timestamp.valueOf(localDate.plusMonths(1).atTime(LocalTime.MIDNIGHT));
        String timetableInfoMSG = String.format(
                "Teacher's timetable for month by date with  teacherId %d and date between %s and %s", teacherId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<TimetableDTO> unsortedDayTimetable = new ArrayList<>();
        try {
            unsortedDayTimetable.addAll(jdbcTemplate.query(GET_TEACHER_MONTH_TIMETABLE,
                    new Object[] { teacherId, timeFrom, timeTo }, timetableMapper));
            LOGGER.info("Successfully retrived {}", timetableInfoMSG);
        } catch (DataAccessException e) {
            String failMessage = "Failed to get " + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return unsortedDayTimetable.stream().sorted(Comparator.comparing(TimetableDTO::getDate))
                .collect(Collectors.toList());
    }

    private Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }

}
