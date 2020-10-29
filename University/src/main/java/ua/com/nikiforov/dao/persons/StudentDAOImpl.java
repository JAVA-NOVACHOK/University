package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.*;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.persons.StudentMapper;
import ua.com.nikiforov.models.persons.Student;

@Repository
public class StudentDAOImpl implements StudentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAOImpl.class);

    private static final String ADD_STUDENT = INSERT + TABLE_STUDENTS + L_BRACKET + FIRST_NAME + COMA + LAST_NAME + COMA
            + GROUP_ID + VALUES_3_QMARK;
    private static final String FIND_STUDENT_BY_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_STUDENT_BY_GROUP_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE + GROUP_ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_STUDENT_BY_NAME_GROUP_ID = SELECT + ASTERISK + FROM + TABLE_STUDENTS + WHERE
            + FIRST_NAME + EQUALS_M + Q_MARK + AND + LAST_NAME + EQUALS_M + Q_MARK + AND + GROUP_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_STUDENTS = SELECT + ASTERISK + FROM + TABLE_STUDENTS;
    private static final String UPDATE_STUDENT = UPDATE + TABLE_STUDENTS + SET + FIRST_NAME + EQUALS_M + Q_MARK + COMA
            + LAST_NAME + EQUALS_M + Q_MARK + COMA + GROUP_ID + EQUALS_M + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_STUDENT_BY_ID = DELETE + FROM + TABLE_STUDENTS + WHERE + ID + EQUALS_M + Q_MARK;

    private StudentMapper studentMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDAOImpl(DataSource dataSource, StudentMapper studentMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentMapper = studentMapper;
    }

    @Override
    public boolean addStudent(String firstName, String lastName, long groupId) {
        String studentMessage = String.format("Student with firstName = %s, lastname = %s, groupId = %d", firstName,
                lastName, groupId);
        LOGGER.debug("Adding {}", studentMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_STUDENT, firstName, lastName, groupId) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding {}", studentMessage);
            } else {
                throw new DataOperationException("Couldn't add " + studentMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to add %s", studentMessage);
            LOGGER.error(failMessage, e);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public Student getStudentById(long studentId) {
        LOGGER.debug("Getting Student by id '{}'", studentId);
        Student student;
        try {
            student = jdbcTemplate.queryForObject(FIND_STUDENT_BY_ID, new Object[] { studentId }, studentMapper);
            LOGGER.info("Successfully retrived Student {}", student);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get Student by Id %d", studentId);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return student;
    }

    @Override
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        String studentMessage = String.format("Student with firstName = %s, lastname = %s, groupId = %d", firstName,
                lastName, groupId);
        LOGGER.debug("Getting {}", studentMessage);
        Student student;
        try {
            student = jdbcTemplate.queryForObject(FIND_STUDENT_BY_NAME_GROUP_ID,
                    new Object[] { firstName, lastName, groupId }, studentMapper);
            LOGGER.info("Successfully retrived Student {}", student);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get %s", studentMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        LOGGER.debug("Getting all students");
        List<Student> allStudents = new ArrayList<>();
        try {
            allStudents.addAll(jdbcTemplate.query(GET_ALL_STUDENTS, studentMapper));
            LOGGER.info("Successfully query for all students");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all students from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allStudents;
    }

    @Override
    public boolean updateStudent(String firstName, String lastName, long groupId, long studentId) {
        String studentMessage = String.format("Student with ID = %d and firstName = %s, lastname = %s, groupId = %d",
                studentId, firstName, lastName, groupId);
        LOGGER.debug("Updating {}", studentMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_STUDENT, firstName, lastName, groupId, studentId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated {}", studentMessage);
            } else {
                throw new DataOperationException("Couldn't update " + studentMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to update %s", studentMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteStudentById(long studentId) {
        String studentMessage = String.format("Student by id %d", studentId);
        LOGGER.debug("Deleting {}", studentMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_STUDENT_BY_ID, studentId) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting {}", studentMessage);
            } else {
                throw new DataOperationException("Couldn't delete " + studentMessage);
            }
        } catch (DataAccessException e) {
            String failDeleteMessage = "Failed to delete " + studentMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }
    
    @Override
    public List<Student> getStudentsByGroupId(long groupId) {
        String studentsInGroupMSG = String.format("students from group with ID = %d", groupId);
        LOGGER.debug("Getting {}",studentsInGroupMSG);
        List<Student> studentsInGroup = new ArrayList<>();
        try {
            studentsInGroup.addAll(jdbcTemplate.query(FIND_STUDENT_BY_GROUP_ID, new Object[] {groupId}, studentMapper));
            LOGGER.info("Successfully query {}",studentsInGroupMSG);
        } catch (DataAccessException e) {
            String failMessage = String.format("Fail to get %s",studentsInGroupMSG);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return studentsInGroup;
    }
}
