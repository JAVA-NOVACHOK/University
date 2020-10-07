package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.persons.StudentMapper;
import ua.com.nikiforov.models.persons.Student;

public class StudentDAO {

    private static final String ADD_STUDENT = INSERT + TABLE_STUDENTS + L_BRACKET + COLUMN_STUDENT_FIRST_NAME + COMA
            + COLUMN_STUDENT_LAST_NAME + COMA + COLUMN_STUDENT_GROUP_ID + VALUES_3_QMARK;
    private static final String GET_STUDENT_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE
            + COLUMN_STUDENT_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS = SELECT + ASTERISK + FROM + TABLE_STUDENTS;
    private static final String UPDATE_STUDENT = UPDATE + TABLE_STUDENTS + SET + COLUMN_STUDENT_FIRST_NAME + EQUALS_M
            + Q_MARK + COMA + COLUMN_STUDENT_LAST_NAME + EQUALS_M + Q_MARK + COMA + COLUMN_STUDENT_GROUP_ID + WHERE
            + COLUMN_STUDENT_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_STUDENT_BY_ID = DELETE + ASTERISK + FROM + TABLE_STUDENTS + WHERE
            + COLUMN_STUDENT_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addStudent(String firstName, String lastName, long groupId) {
        return jdbcTemplate.update(ADD_STUDENT, firstName, lastName, groupId) > 0;
    }

    public Student getStudentById(long id) {
        return jdbcTemplate.queryForObject(GET_STUDENT_BY_ID, new Object[] { id }, new StudentMapper());
    }

    public List<Student> getAllStudents() {
        return jdbcTemplate.query(GET_ALL_STUDENTS, new StudentMapper());
    }

    public boolean updateStudent(String firstName, String lastName, long groupId) {
        return jdbcTemplate.update(UPDATE_STUDENT, firstName, lastName, groupId) > 0;
    }

    public boolean deleteStudentById(long id) {
        return jdbcTemplate.update(DELETE_STUDENT_BY_ID, id) > 0;
    }
}
