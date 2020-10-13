package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.persons.StudentMapper;
import ua.com.nikiforov.models.persons.Student;

@Repository
public class StudentDAOImpl implements StudentDAO {

    private static final String ADD_STUDENT = INSERT + TABLE_STUDENTS + L_BRACKET + FIRST_NAME + COMA + LAST_NAME + COMA
            + GROUP_ID + VALUES_3_QMARK;
    private static final String FIND_STUDENT_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_STUDENT_BY_NAME_GROUP_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE
            + FIRST_NAME + EQUALS_M + Q_MARK + AND + LAST_NAME + EQUALS_M + Q_MARK + AND + GROUP_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS = SELECT + ASTERISK + FROM + TABLE_STUDENTS;
    private static final String UPDATE_STUDENT = UPDATE + TABLE_STUDENTS + SET + FIRST_NAME + EQUALS_M + Q_MARK + COMA
            + LAST_NAME + EQUALS_M + Q_MARK + COMA + GROUP_ID + EQUALS_M + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_STUDENT_BY_ID = DELETE + FROM + TABLE_STUDENTS + WHERE + ID + EQUALS_M
            + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addStudent(String firstName, String lastName, long groupId) {
        return jdbcTemplate.update(ADD_STUDENT, firstName, lastName, groupId) > 0;
    }

    @Override
    public Student getStudentById(long studentId) {
        return jdbcTemplate.queryForObject(FIND_STUDENT_BY_ID, new Object[] { studentId }, new StudentMapper());
    }

    @Override
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        return jdbcTemplate.queryForObject(FIND_STUDENT_BY_NAME_GROUP_ID, new Object[] { firstName, lastName, groupId },
                new StudentMapper());
    }

    @Override
    public List<Student> getAllStudents() {
        return jdbcTemplate.query(GET_ALL_STUDENTS, new StudentMapper());
    }

    @Override
    public boolean updateStudent(String firstName, String lastName, long groupId, long studentId) {
        return jdbcTemplate.update(UPDATE_STUDENT, firstName, lastName, groupId, studentId) > 0;
    }

    @Override
    public boolean deleteStudentById(long studentId) {
        return jdbcTemplate.update(DELETE_STUDENT_BY_ID, studentId) > 0;
    }
}
