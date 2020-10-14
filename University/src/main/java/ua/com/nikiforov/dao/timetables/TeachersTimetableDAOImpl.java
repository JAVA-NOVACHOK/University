package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.timetables.TeacherTimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.timetables.Period;

@Repository
@Qualifier("teachersTimetableDAO")
public class TeachersTimetableDAOImpl implements TimetableDAO {

    private static final String ADD_TEACHERS_TIMETABLE = INSERT + TABLE_TEACHERS_TIMETABLE + L_BRACKET + LESSON_ID
            + COMA + PERSON_ID + COMA + DATE + COMA + PERIOD + VALUES_4_QMARK;
    private static final String FIND_TEACHERS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE;
    private static final String UPDATE_TEACHERS_TIMETABLE = UPDATE + TABLE_TEACHERS_TIMETABLE + SET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + DATE + EQUALS_M + Q_MARK
            + VALUES_3_QMARK;
    private static final String DELETE_TEACHERS_TIMETABLE_BY_ID = DELETE + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
//    0 "AND p.examDate BETWEEN STR_TO_DATE(?3,'%Y,%m,%d') AND STR_TO_DATE(?4,'%Y,%m,%d')")

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeachersTimetableDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addTimetable(long lessonId, long teacherId, String stringDate,Period period) {
        LocalDate time = getLocalDateFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(ADD_TEACHERS_TIMETABLE, lessonId, teacherId, periodNumber) > 0;
    }

    @Override
    public Timetable getTimetableById(long id) {
        return jdbcTemplate.queryForObject(FIND_TEACHERS_TIMETABLE_BY_ID, new Object[] { id },
                new TeacherTimetableMapper());
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return jdbcTemplate.query(GET_ALL_TEACHERS_TIMETABLE, new TeacherTimetableMapper());
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String stringDate, Period period, long id) {
        LocalDate time = getLocalDateFromString(stringDate);
        int periodNumber = period.getPeriod();
        return jdbcTemplate.update(UPDATE_TEACHERS_TIMETABLE, lessonId, teacherId, periodNumber, id) > 0;
    }

    @Override
    public boolean deleteTimetableById(long id) {
        return jdbcTemplate.update(DELETE_TEACHERS_TIMETABLE_BY_ID, id) > 0;
    }
    
    private LocalDate getLocalDateFromString(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDate.parse(stringDate, formatter);
    }


    @Override
    public List<Timetable> getMonthTimetable(String date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Timetable> getDayTimetable(String date) {
        // TODO Auto-generated method stub
        return null;
    }

}
