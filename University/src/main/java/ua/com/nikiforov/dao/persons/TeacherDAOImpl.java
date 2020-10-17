package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.FIRST_NAME;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.LAST_NAME;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.ID;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.TABLE_TEACHERS;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private static final String ADD_TEACHER = INSERT + TABLE_TEACHERS + L_BRACKET + FIRST_NAME + COMA + LAST_NAME
            + VALUES_2_QMARK;
    private static final String FIND_TEACHER_BY_ID = SELECT + ASTERISK + FROM + TABLE_TEACHERS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String GET_TEACHER_BY_NAME = SELECT + ASTERISK + FROM + TABLE_TEACHERS + WHERE + FIRST_NAME
            + EQUALS_M + Q_MARK + AND + LAST_NAME + EQUALS_M + Q_MARK;
    private static final String GET_ALL_TEACHERS = SELECT + ASTERISK + FROM + TABLE_TEACHERS;
    private static final String UPDATE_TEACHER = UPDATE + TABLE_TEACHERS + SET + FIRST_NAME + EQUALS_M + Q_MARK + COMA
            + LAST_NAME + EQUALS_M + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_TEACHER_BY_ID = DELETE + FROM + TABLE_TEACHERS + WHERE + ID + EQUALS_M + Q_MARK;

    private TeacherMapper teacherMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherDAOImpl(DataSource dataSource,TeacherMapper teacherMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.teacherMapper = teacherMapper;
    }

    @Override
    public boolean addTeacher(String firstName, String lastName) {
        return jdbcTemplate.update(ADD_TEACHER, firstName, lastName) > 0;
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        return jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, new Object[] { teacherId }, teacherMapper);
    }

    @Override
    public Teacher getTeacherByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject(GET_TEACHER_BY_NAME, new Object[] { firstName, lastName },
                teacherMapper);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return jdbcTemplate.query(GET_ALL_TEACHERS, teacherMapper);
    }

    @Override
    public boolean updateTeacher(String firstName, String lastName, long teacherId) {
        return jdbcTemplate.update(UPDATE_TEACHER, firstName, lastName, teacherId) > 0;
    }

    @Override
    public boolean deleteTeacherById(long teacherId) {
        return jdbcTemplate.update(DELETE_TEACHER_BY_ID, teacherId) > 0;
    }
}
