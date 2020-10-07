package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.persons.Teacher;

public class TeacherDAO {

    private static final String ADD_TEACHER = INSERT + TABLE_TEACHERS + L_BRACKET + COLUMN_TEACHER_FIRST_NAME + COMA
            + COLUMN_TEACHER_LAST_NAME + COMA + COLUMN_TEACHER_SUBJECT_ID + VALUES_3_QMARK;
    private static final String GET_TEACHER_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS + WHERE
            + COLUMN_TEACHER_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS = SELECT + ASTERISK + FROM + TABLE_TEACHERS;
    private static final String UPDATE_TEACHER = UPDATE + TABLE_TEACHERS + SET + COLUMN_TEACHER_FIRST_NAME + EQUALS_M
            + Q_MARK + COMA + COLUMN_TEACHER_LAST_NAME + EQUALS_M + Q_MARK + COMA + COLUMN_TEACHER_SUBJECT_ID + WHERE
            + COLUMN_TEACHER_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_TEACHER_BY_ID = DELETE + ASTERISK + FROM + TABLE_TEACHERS + WHERE
            + COLUMN_TEACHER_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addTeacher(String firstName, String lastName, long groupId) {
        return jdbcTemplate.update(ADD_TEACHER, firstName, lastName, groupId) > 0;
    }

    public Teacher getTeacherById(long id) {
        return jdbcTemplate.queryForObject(GET_TEACHER_BY_ID, new Object[] { id }, new TeacherMapper());
    }

    public List<Teacher> getAllTeachers() {
        return jdbcTemplate.query(GET_ALL_TEACHERS, new TeacherMapper());
    }

    public boolean updateTeacher(String firstName, String lastName, long groupId) {
        return jdbcTemplate.update(UPDATE_TEACHER, firstName, lastName, groupId) > 0;
    }

    public boolean deleteTeacherById(long id) {
        return jdbcTemplate.update(DELETE_TEACHER_BY_ID, id) > 0;
    }
}
