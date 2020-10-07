package ua.com.nikiforov.dao.timetables;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.time.Instant;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.timetables.StudentsTimetableMapper;
import ua.com.nikiforov.models.timetables.StudentsTimetable;

public class StudentsTimetableDAO {

    private static final String ADD_STUDENTS_TIMETABLE = INSERT + TABLE_STUDENTS_TIMETABLE + L_BRACKET
            + COLUMN_STUDENTS_TIMETABLE_LESSON_ID + EQUALS_M + Q_MARK + COMA + COLUMN_STUDENTS_TIMETABLE_STUDENT_ID
            + EQUALS_M + Q_MARK + COMA + COLUMN_STUDENTS_TIMETABLE_TIME + EQUALS_M + Q_MARK + VALUES_3_QMARK;
    private static final String FIND_STUDENTS_TIMETABLE_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + COLUMN_STUDENTS_TIMETABLE_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS_TIMETABLE = SELECT + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE;
    private static final String UPDATE_STUDENTS_TIMETABLE = UPDATE + TABLE_STUDENTS_TIMETABLE + SET
            + COLUMN_STUDENTS_TIMETABLE_LESSON_ID + EQUALS_M + Q_MARK + COMA + COLUMN_STUDENTS_TIMETABLE_STUDENT_ID
            + EQUALS_M + Q_MARK + COMA + COLUMN_STUDENTS_TIMETABLE_TIME + EQUALS_M + Q_MARK + VALUES_3_QMARK;
    private static final String DELETE_STUDENTS_TIMETABLE_BY_ID = DELETE + ASTERISK + FROM + TABLE_STUDENTS_TIMETABLE
            + WHERE + COLUMN_STUDENTS_TIMETABLE_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsTimetableDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addStudentsTimetable(long lessonId, long studentId, Instant time) {
        return jdbcTemplate.update(ADD_STUDENTS_TIMETABLE, lessonId, studentId, time) > 0;
    }

    public StudentsTimetable getStudentTimetableById(long id) {
        return jdbcTemplate.queryForObject(FIND_STUDENTS_TIMETABLE_BY_ID, new Object[] { id },
                new StudentsTimetableMapper());
    }

    public List<StudentsTimetable> getAllStudentsTimetables() {
        return jdbcTemplate.query(GET_ALL_STUDENTS_TIMETABLE, new StudentsTimetableMapper());
    }

    public boolean updateStudentsTimetable(long lessonId, long studentId, Instant time, long id) {
        return jdbcTemplate.update(UPDATE_STUDENTS_TIMETABLE, lessonId, studentId, time, id) > 0;
    }

    public boolean deleteStudentsTimetableById(long id) {
        return jdbcTemplate.update(DELETE_STUDENTS_TIMETABLE_BY_ID, id) > 0;
    }

}
