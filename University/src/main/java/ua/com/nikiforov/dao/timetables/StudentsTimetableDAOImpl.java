package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.*;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.timetables.StudentTimetableMapper;
import ua.com.nikiforov.mappers.timetables.TimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

@Repository
@Qualifier("studentTimtableDao")
public class StudentsTimetableDAOImpl implements TimetableDAO {

    private static final int DATE_STATEMENT_INDEX = 1;
    private static final int FROM_DATE_STATEMENT_INDEX = 2;
    private static final int TO_DATE_STATEMENT_INDEX = 3;
    private static final int STUDENT_ID_STATEMENT_INDEX = 2;
    private static final int STUDENT_ID_STATEMENT_INDEX_GET_MONTH_TABLE = 1;

    private static final String ADD_STUDENTS_TIMETABLE = INSERT + TABLE_STUDENTS_TIMETABLE + L_BRACKET + LESSON_ID
            + COMA + PERSON_ID + COMA + DATE + COMA + PERIOD + VALUES_4_QMARK;
    private static final String FIND_STUDENTS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsTimetableDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addTimetable(long lessonId, long studentId, String stringDate, Period period) {
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(ADD_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber) > 0;
    }

    @Override
    public Timetable getTimetableById(long timetableId) {
        return jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_ID, new Object[] { timetableId },
                new TimetableMapper());
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return jdbcTemplate.query(GET_ALL_STUDENTS_TIMETABLE, new StudentTimetableMapper());
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
        }, new TimetableMapper());
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
        }, new TimetableMapper());
    }
}
