package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.*;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import ua.com.nikiforov.exceptions.EntityNotFoundException;
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
    private static final String GETTING = "Getting {}";
    private static final String FAILED_TO_GET = "Failed to get %s";

    private TimetableMapper timetableMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsTimetableDAOImpl(DataSource dataSource, TimetableMapper timetableMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public boolean addTimetable(long lessonId, long studentId, String stringDate, Period period) {
        String timetableMessage = String.format("TeachersTimetable with lessonId = %d, sudentId = %d, date = %s, period = %d",
                lessonId, studentId, stringDate, period.getPeriod());
        LOGGER.debug("Adding {}", timetableMessage);
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber) > 0;
            if (actionResult) {
                LOGGER.info("Successfully added {}", timetableMessage);
            } else {
                throw new DataOperationException("Couldn't add " + timetableMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to add %s", timetableMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return actionResult;
    }

    @Override
    public Timetable getTimetableById(long timetableId) {
        String timetableMessage = String.format("TeachersTimetable by Id %d", timetableId);
        LOGGER.debug(GETTING, timetableMessage);
        Timetable timetable;
        try {
            timetable = jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_ID, new Object[] { timetableId },
                    timetableMapper);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format(FAILED_TO_GET, timetableMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        return timetable;
    }

    @Override
    public Timetable getTimetableByLessonPersonTimePeriod(long lessonId, long studentId, String stringDate,
            Period period) {
        String timetableMessage = String.format("TeachersTimetable by lessonId = %d, sudentId = %d, date = %s, period = %d",
                lessonId, studentId, stringDate, period.getPeriod());
        LOGGER.debug(GETTING, timetableMessage);
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        Timetable timetable;
        try {
            timetable = jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_LESSON_TEACHER_TIME_PERIOD,
                    new Object[] { lessonId, studentId, time, periodNumber }, timetableMapper);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format(FAILED_TO_GET, timetableMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        return timetable;
    }

    @Override
    public List<Timetable> getAllTimetables() {
        LOGGER.debug("Getting all timetables");
        List<Timetable> allTimetables = new ArrayList<>();
        try {
            allTimetables.addAll(jdbcTemplate.query(GET_ALL_STUDENTS_TIMETABLE, timetableMapper));
            LOGGER.info("Successfully query for all timetables");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all timetables from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allTimetables;
    }

    @Override
    public boolean updateTimetable(long lessonId, long studentId, String stringDate, Period period,
            long studentsTimetableId) {
        String timetableMessage = String.format(
                "TeachersTimetable with ID = %d and lessonId = %d, sudentId = %d, date = %s, period = %d", studentsTimetableId,
                lessonId, studentId, stringDate, period.getPeriod());
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        LOGGER.debug("Updating {}", timetableMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber,
                    studentsTimetableId) > 0;
            if (actionResult) {
                LOGGER.info("Successfylly updated {}", timetableMessage);
            } else {
                throw new DataOperationException("Couldn't update " + timetableMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = "Failed to update" + timetableMessage;
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return actionResult;
    }

    @Override
    public boolean deleteTimetableById(long studentsTimetableId) {
        String studentsTimetablMSG = String.format("student's timetable by id %d", studentsTimetableId);
        LOGGER.debug("Deleting {}", studentsTimetablMSG);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_STUDENTS_TIMETABLE_BY_ID, studentsTimetableId) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting '{}'", studentsTimetablMSG);
            } else {
                throw new EntityNotFoundException("Couldn't delete " + studentsTimetablMSG);
            }
        } catch (DataAccessException e) {
            String failDeleteMessage = String.format("Failed to delete %s", studentsTimetablMSG);
            LOGGER.error(failDeleteMessage);
            throw new EntityNotFoundException(failDeleteMessage, e);
        }
        return actionResult;
    }

    public List<Timetable> getDayTimetable(String date, long studentId) {
        String dayTimetableMSG = String.format("TeachersTimetable for day by date %s and studentId %d", date, studentId);
        LOGGER.debug(GETTING, date);
        List<Timetable> dayTimetable = new ArrayList<>();
        try {
            dayTimetable.addAll(jdbcTemplate.query(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_DAY_TIMETABLE);
                preparedStatement.setTimestamp(DATE_STATEMENT_INDEX, getTimestampFromString(date));
                preparedStatement.setLong(STUDENT_ID_STATEMENT_INDEX, studentId);
                return preparedStatement;
            }, timetableMapper));
            LOGGER.info("Got {}", dayTimetableMSG);
        } catch (DataAccessException e) {
            String failMessage = String.format(FAILED_TO_GET, dayTimetableMSG);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return dayTimetable;
    }

    @Override
    public List<Timetable> getMonthTimetable(String date, long studentId) {
        String monthTimetableMSG = String.format("TeachersTimetable for day by date %s and studentId %d", date, studentId);
        LOGGER.debug(GETTING, date);
        List<Timetable> monthTimetable = new ArrayList<>();
        Timestamp timestampFrom = Timestamp.valueOf(date + " 00:00:00");
        LocalDate localDate = timestampFrom.toLocalDateTime().toLocalDate();
        Timestamp timestampTo = Timestamp.valueOf(localDate.plusMonths(1).atTime(LocalTime.MIDNIGHT));
        try {
            monthTimetable.addAll(jdbcTemplate.query(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_MONTH_TIMETABLE);
                preparedStatement.setLong(STUDENT_ID_STATEMENT_INDEX_GET_MONTH_TABLE, studentId);
                preparedStatement.setTimestamp(FROM_DATE_STATEMENT_INDEX, timestampFrom);
                preparedStatement.setTimestamp(TO_DATE_STATEMENT_INDEX, timestampTo);
                return preparedStatement;
            }, timetableMapper));
            LOGGER.info("Got {}", monthTimetableMSG);
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to %s", monthTimetableMSG);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return monthTimetable;
    }

    private Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }
}
