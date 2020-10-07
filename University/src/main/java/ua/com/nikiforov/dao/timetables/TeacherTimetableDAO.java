package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.time.Instant;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.timetables.TeachersTimetableMapper;
import ua.com.nikiforov.models.timetables.TeachersTimetable;

public class TeacherTimetableDAO {

    private static final String ADD_TEACHERS_TIMETABLE = INSERT + TABLE_TEACHERS_TIMETABLE + L_BRACKET
            + COLUMN_TEACHERS_TIMETABLE_LESSON_ID + EQUALS_M + Q_MARK + COMA + COLUMN_TEACHERS_TIMETABLE_TEACHER_ID
            + EQUALS_M + Q_MARK + COMA + COLUMN_TEACHERS_TIMETABLE_TIME + EQUALS_M + Q_MARK + VALUES_3_QMARK;
    private static final String FIND_TEACHERS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + COLUMN_TEACHERS_TIMETABLE_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE;
    private static final String UPDATE_TEACHERS_TIMETABLE = UPDATE + TABLE_TEACHERS_TIMETABLE + SET
            + COLUMN_TEACHERS_TIMETABLE_LESSON_ID + EQUALS_M + Q_MARK + COMA + COLUMN_TEACHERS_TIMETABLE_TEACHER_ID
            + EQUALS_M + Q_MARK + COMA + COLUMN_TEACHERS_TIMETABLE_TIME + EQUALS_M + Q_MARK + VALUES_3_QMARK;
    private static final String DELETE_TEACHERS_TIMETABLE_BY_ID = DELETE + ASTERISK + FROM + TABLE_TEACHERS_TIMETABLE
            + WHERE + COLUMN_TEACHERS_TIMETABLE_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
        public TeacherTimetableDAO(DataSource dataSource) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }

    public boolean addTeacherTimetable(long lessonId, long TEACHERId, Instant time) {
        return jdbcTemplate.update(ADD_TEACHERS_TIMETABLE, lessonId, TEACHERId, time) > 0;
    }

    public TeachersTimetable getTEACHERTimetableById(long id) {
        return jdbcTemplate.queryForObject(FIND_TEACHERS_TIMETABLE_BY_ID, new Object[] { id },
                new TeachersTimetableMapper());
    }

    public List<TeachersTimetable> getAllTEACHERSTimetables() {
        return jdbcTemplate.query(GET_ALL_TEACHERS_TIMETABLE, new TeachersTimetableMapper());
    }

    public boolean updateTEACHERSTimetable(long lessonId, long teacherId, Instant time, long id) {
        return jdbcTemplate.update(UPDATE_TEACHERS_TIMETABLE, lessonId, teacherId, time, id) > 0;
    }

    public boolean deleteTEACHERSTimetableById(long id) {
        return jdbcTemplate.update(DELETE_TEACHERS_TIMETABLE_BY_ID, id) > 0;
    }

}


