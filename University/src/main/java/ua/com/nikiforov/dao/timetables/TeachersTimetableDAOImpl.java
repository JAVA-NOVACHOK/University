package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.timetables.TeacherTimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;

@Repository
@Qualifier("teachersTimetableDAO")
public class TeachersTimetableDAOImpl implements TimetableDAO {

    private static final String ADD_TEACHERS_TIMETABLE = INSERT + TABLE_TEACHERS_TIMETABLE + L_BRACKET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + TIME + EQUALS_M + Q_MARK
            + VALUES_3_QMARK;
    private static final String FIND_TEACHERS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE;
    private static final String UPDATE_TEACHERS_TIMETABLE = UPDATE + TABLE_TEACHERS_TIMETABLE + SET + LESSON_ID
            + EQUALS_M + Q_MARK + COMA + PERSON_ID + EQUALS_M + Q_MARK + COMA + TIME + EQUALS_M + Q_MARK
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
    public boolean addTimetable(long lessonId, long teacherId, Instant time) {
        return jdbcTemplate.update(ADD_TEACHERS_TIMETABLE, lessonId, teacherId, time) > 0;
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
    public boolean updateTimetable(long lessonId, long teacherId, Instant time, long id) {
        return jdbcTemplate.update(UPDATE_TEACHERS_TIMETABLE, lessonId, teacherId, time, id) > 0;
    }

    @Override
    public boolean deleteTimetableById(long id) {
        return jdbcTemplate.update(DELETE_TEACHERS_TIMETABLE_BY_ID, id) > 0;
    }

}
