package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.timetables.StudentTimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

@Repository
@Qualifier("studentTimtableDao")
public class StudentsTimetableDAOImpl implements TimetableDAO {

    private static final int DATE_INDEX = 3;

    private static final String ADD_STUDENTS_TIMETABLE = INSERT + TABLE_STUDENTS_TIMETABLE + L_BRACKET + LESSON_ID
            + COMA + PERSON_ID + COMA + DATE + COMA + PERIOD + VALUES_4_QMARK;
    private static final String FIND_STUDENTS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE;
    private static final String UPDATE_STUDENTS_TIMETABLE = UPDATE + TABLE_STUDENTS_TIMETABLE + SET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + DATE + EQUALS_M + Q_MARK
            + VALUES_3_QMARK;
    private static final String DELETE_STUDENTS_TIMETABLE_BY_ID = DELETE + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String GET_DAY_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE + WHERE + DATE
            + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsTimetableDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addTimetable(long lessonId, long studentId, String stringDate, Period period) {
        LocalDate time = getLocalDateFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(ADD_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber) > 0;
    }

    @Override
    public Timetable getTimetableById(long timetableId) {
        return jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_ID, new Object[] { timetableId },
                new StudentTimetableMapper());
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return jdbcTemplate.query(GET_ALL_STUDENTS_TIMETABLE, new StudentTimetableMapper());
    }

    @Override
    public boolean updateTimetable(long lessonId, long studentId, String stringDate, Period period, long studentsTimetableId) {
        LocalDate time = getLocalDateFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(UPDATE_STUDENTS_TIMETABLE, lessonId, studentId, time, periodNumber,studentsTimetableId) > 0;
    }

    @Override
    public boolean deleteTimetableById(long studentsTimetableId) {
        return jdbcTemplate.update(DELETE_STUDENTS_TIMETABLE_BY_ID, studentsTimetableId) > 0;
    }

    public List<Timetable> getDayTimetable(Date time) {
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connnection) throws SQLException {
                final PreparedStatement preparedStatement = connnection.prepareStatement(GET_DAY_TIMETABLE);
                preparedStatement.setDate(DATE_INDEX, time);
                return preparedStatement;
            }
        }, new StudentTimetableMapper());
    }
    
    private LocalDate getLocalDateFromString(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDate.parse(stringDate, formatter);
    }

}
