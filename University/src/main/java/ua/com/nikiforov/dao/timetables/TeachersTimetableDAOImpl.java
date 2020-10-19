package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.DATE;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.PERSON_ID;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

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

import ua.com.nikiforov.mappers.timetables.TimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

@Repository
@Qualifier("teachersTimetableDAO")
public class TeachersTimetableDAOImpl implements TimetableDAO {

    private static final int DATE_STATEMENT_INDEX = 1;
    private static final int FROM_DATE_STATEMENT_INDEX = 2;
    private static final int TO_DATE_STATEMENT_INDEX = 3;
    private static final int STUDENT_ID_STATEMENT_INDEX = 2;
    private static final int STUDENT_ID_STATEMENT_INDEX_GET_MONTH_TABLE = 1;

    private static final String ADD_TEACHERS_TIMETABLE = INSERT + TABLE_TEACHERS_TIMETABLE + L_BRACKET + LESSON_ID
            + COMA + PERSON_ID + COMA + DATE + COMA + PERIOD + VALUES_4_QMARK;
    private static final String FIND_TEACHERS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String FIND_TEACHERS_TIMETABLE_BY_LESSON_TEACHER_TIME_PERIOD = SELECT + ASTERISK + FROM
            + TABLE_TEACHERS_TIMETABLE + WHERE + LESSON_ID + EQUALS_M + Q_MARK + AND + PERSON_ID + EQUALS_M + Q_MARK
            + AND + DATE + EQUALS_M + Q_MARK + AND + PERIOD + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE;
    private static final String UPDATE_TEACHERS_TIMETABLE = UPDATE + TABLE_TEACHERS_TIMETABLE + SET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + DATE + EQUALS_M + Q_MARK + COMA + PERIOD
            + EQUALS_M + Q_MARK + WHERE + PERSON_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_TEACHERS_TIMETABLE_BY_ID = DELETE + FROM + TABLE_TEACHERS_TIMETABLE + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String GET_DAY_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE + WHERE + DATE
            + EQUALS_M + Q_MARK + AND + PERSON_ID + EQUALS_M + Q_MARK;
    private static final String GET_MONTH_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE + WHERE
            + PERSON_ID + EQUALS_M + Q_MARK + AND + DATE + BETWEEN + Q_MARK + AND + Q_MARK;

    private TimetableMapper timetableMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeachersTimetableDAOImpl(DataSource dataSource, TimetableMapper timetableMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public boolean addTimetable(long lessonId, long teacherId, String stringDate, Period period) {
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(ADD_TEACHERS_TIMETABLE, lessonId, teacherId, time, periodNumber) > 0;
    }

    @Override
    public Timetable getTimetableById(long id) {
        return jdbcTemplate.queryForObject(FIND_TEACHERS_TIMETABLE_BY_ID, new Object[] { id },
                timetableMapper);
    }

    @Override
    public Timetable getTimetableByLessonTeacherTimePeriod(long lessonId, long teacherId, String stringDate,
            Period period) {
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.queryForObject(FIND_TEACHERS_TIMETABLE_BY_LESSON_TEACHER_TIME_PERIOD,
                new Object[] { lessonId, teacherId, time, periodNumber }, timetableMapper);
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return jdbcTemplate.query(GET_ALL_TEACHERS_TIMETABLE, timetableMapper);
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String stringDate, Period period, long id) {
        Timestamp time = getTimestampFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(UPDATE_TEACHERS_TIMETABLE, lessonId, teacherId, time, periodNumber, id) > 0;
    }

    @Override
    public boolean deleteTimetableById(long id) {
        return jdbcTemplate.update(DELETE_TEACHERS_TIMETABLE_BY_ID, id) > 0;
    }

    public List<Timetable> getDayTimetable(String date, long studentId) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_DAY_TIMETABLE);
            preparedStatement.setTimestamp(DATE_STATEMENT_INDEX, getTimestampFromString(date));
            preparedStatement.setLong(STUDENT_ID_STATEMENT_INDEX, studentId);
            return preparedStatement;
        }, timetableMapper);
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

    private Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }

}
