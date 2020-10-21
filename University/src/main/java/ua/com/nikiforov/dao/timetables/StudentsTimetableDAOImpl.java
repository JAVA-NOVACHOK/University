package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.*;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers.timetables.TimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

@Repository
@Qualifier("studentTimtableDao")
public class StudentsTimetableDAOImpl implements TimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsTimetableDAOImpl.class);

    private static final int DATE_STATEMENT_INDEX = 1;
    private static final int FROM_DATE_STATEMENT_INDEX = 2;
    private static final int TO_DATE_STATEMENT_INDEX = 3;
    private static final int STUDENT_ID_STATEMENT_INDEX = 2;
    private static final int STUDENT_ID_STATEMENT_INDEX_GET_MONTH_TABLE = 1;

    private static final String ADD_STUDENTS_TIMETABLE = INSERT + TABLE_STUDENTS_TIMETABLE + L_BRACKET + LESSON_ID
            + COMA + PERSON_ID + COMA + DATE + COMA + PERIOD + VALUES_4_QMARK;
    private static final String FIND_STUDENTS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String FIND_STUDENTS_TIMETABLE_BY_LESSON_TEACHER_TIME_PERIOD = SELECT + ASTERISK + FROM
            + TABLE_STUDENTS_TIMETABLE + WHERE + LESSON_ID + EQUALS_M + Q_MARK + AND + PERSON_ID + EQUALS_M + Q_MARK
            + AND + DATE + EQUALS_M + Q_MARK + AND + PERIOD + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE;
    private static final String UPDATE_STUDENTS_TIMETABLE = UPDATE + TABLE_STUDENTS_TIMETABLE + SET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + DATE + EQUALS_M + Q_MARK + COMA + PERIOD
            + EQUALS_M + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_STUDENTS_TIMETABLE_BY_ID = DELETE + FROM + TABLE_STUDENTS_TIMETABLE + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String GET_DAY_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE + WHERE + DATE
            + EQUALS_M + Q_MARK + AND + PERSON_ID + EQUALS_M + Q_MARK;
    private static final String GET_MONTH_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE + WHERE
            + PERSON_ID + EQUALS_M + Q_MARK + AND + DATE + BETWEEN + Q_MARK + AND + Q_MARK;

    private TimetableMapper timetableMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsTimetableDAOImpl(DataSource dataSource, TimetableMapper timetableMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public boolean addTimetable(long lessonId, long studentId, String stringDate, Period period) {
        String timetableMessage = String.format("Timetable with lessonId = %d, sudentId = %d, date = %s, period = %d",
                lessonId, studentId, stringDate, period.getPeriod());
        LOGGER.debug("Adding {}", timetableMessage);
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber) > 0;
            if (actionResult) {
                LOGGER.info("Successfully added {}", timetableMessage);
            }else {
                LOGGER.error("Couldn't add {}",timetableMessage);
                throw new DataOperationException(message)
            }
        } catch (DataAccessException e) {

        }
        return actionResult;
    }

    @Override
    public Timetable getTimetableById(long timetableId) {
        String timetableMessage = String.format("Timetable by Id %d", timetableId);
        LOGGER.debug("Getting {}", timetableMessage);
        Timetable timetable;
        try {
            timetable = jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_ID, new Object[] { timetableId },
                    timetableMapper);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Failed to get %s", timetableMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return timetable;
    }

    @Override
    public Timetable getTimetableByLessonPersonTimePeriod(long lessonId, long studentId, String stringDate,
            Period period) {
        String timetableMessage = String.format("Timetable by lessonId = %d, sudentId = %d, date = %s, period = %d",
                lessonId, studentId, stringDate, period.getPeriod());
        LOGGER.debug("Getting {}", timetableMessage);
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        Timetable timetable;
        try {
            timetable = jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_LESSON_TEACHER_TIME_PERIOD,
                    new Object[] { lessonId, studentId, time, periodNumber }, timetableMapper);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Failed to get %s", timetableMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return timetable;
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return jdbcTemplate.query(GET_ALL_STUDENTS_TIMETABLE, timetableMapper);
    }

    @Override
    public boolean updateTimetable(long lessonId, long studentId, String stringDate, Period period,
            long studentsTimetableId) {
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(UPDATE_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber,
                studentsTimetableId) > 0;
    }

    @Override
    public boolean deleteTimetableById(long studentsTimetableId) {
        return jdbcTemplate.update(DELETE_STUDENTS_TIMETABLE_BY_ID, studentsTimetableId) > 0;
    }

    public List<Timetable> getDayTimetable(String date, long studentId) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_DAY_TIMETABLE);
            preparedStatement.setTimestamp(DATE_STATEMENT_INDEX, getTimestampFromString(date));
            preparedStatement.setLong(STUDENT_ID_STATEMENT_INDEX, studentId);
            return preparedStatement;
        }, timetableMapper);
    }

    private Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }

    @Override
    public List<Timetable> getMonthTimetable(String stringDate, long studentId) {
        Timestamp timestampFrom = Timestamp.valueOf(stringDate + " 00:00:00");
        LocalDate localDate = timestampFrom.toLocalDateTime().toLocalDate();
        Timestamp timestampTo = Timestamp.valueOf(localDate.plusMonths(1).atTime(LocalTime.MIDNIGHT));
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MONTH_TIMETABLE);
            preparedStatement.setLong(STUDENT_ID_STATEMENT_INDEX_GET_MONTH_TABLE, studentId);
            preparedStatement.setTimestamp(FROM_DATE_STATEMENT_INDEX, timestampFrom);
            preparedStatement.setTimestamp(TO_DATE_STATEMENT_INDEX, timestampTo);
            return preparedStatement;
        }, timetableMapper);
    }
}
