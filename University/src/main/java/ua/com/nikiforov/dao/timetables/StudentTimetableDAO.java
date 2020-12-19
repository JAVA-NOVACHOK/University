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

import ua.com.nikiforov.dto.TimetableDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers.timetables.TimetableMapper;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

@Repository
@Qualifier("studentTimetable")
public class StudentTimetableDAO implements TimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentTimetableDAO.class);

    private static final String GET_STUDENT_DAY_TIMETABLE = "SELECT period, subject_name, "
            + "room_number, group_name, time, first_name, last_name,lesson_id,teachers.teacher_id " + "FROM lessons "
            + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.group_id = ? AND time = ? ORDER BY period";

    private static final String GET_STUDENT_MONTH_TIMETABLE = "SELECT period, subject_name, "
            + "room_number, group_name, time, first_name, last_name,lesson_id,teachers.teacher_id " + "FROM lessons "
            + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.group_id = ? AND time BETWEEN ? AND ? ORDER BY time";

    private static final String FAILED_MSG = "Failed to get ";
    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIVED_MSG = "Successfully retrived {}";

    private JdbcTemplate jdbcTemplate;
    private TimetableMapper timetableMapper;

    @Autowired
    public StudentTimetableDAO(DataSource dataSource, TimetableMapper timetableMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public List<TimetableDTO> getDayTimetable(String date, long groupId) {
        String timetableInfoMSG = String.format("Student's timetable for day by date with such data: %s and groupId %d",
                date, groupId);
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<TimetableDTO> dayTimetable = new ArrayList<>();
        Timestamp time = PersonalTimetable.getTimestampFromString(date);
        try {
            dayTimetable.addAll(
                    jdbcTemplate.query(GET_STUDENT_DAY_TIMETABLE, new Object[] { groupId, time }, timetableMapper));
            LOGGER.info(SUCCESSFULLY_RETRIVED_MSG, timetableInfoMSG);
        } catch (DataAccessException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return dayTimetable;
    }

    @Override
    public List<TimetableDTO> getMonthTimetable(String date, long groupId) {
        Timestamp timeFrom = PersonalTimetable.getTimestampFromString(date);
        LocalDate localDate = timeFrom.toLocalDateTime().toLocalDate();
        Timestamp timeTo = Timestamp.valueOf(localDate.plusMonths(1).atTime(LocalTime.MIDNIGHT));
        String timetableInfoMSG = String.format(
                "Student's timetable for month by date with  groupId %d and date between %s and %s", groupId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug(GETTING_MSG, timetableInfoMSG);
        List<TimetableDTO> unsortedDayTimetable = new ArrayList<>();
        try {
            unsortedDayTimetable.addAll(jdbcTemplate.query(GET_STUDENT_MONTH_TIMETABLE,
                    new Object[] { groupId, timeFrom, timeTo }, timetableMapper));
            LOGGER.info(SUCCESSFULLY_RETRIVED_MSG, timetableInfoMSG);
        } catch (DataAccessException e) {
            String failMessage = FAILED_MSG + timetableInfoMSG;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return unsortedDayTimetable.stream().sorted(Comparator.comparing(TimetableDTO::getDate))
                .collect(Collectors.toList());
    }

   

}
