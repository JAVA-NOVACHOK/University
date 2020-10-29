<<<<<<< HEAD
package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.*;

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
import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAOImpl.class);

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
    public TeacherDAOImpl(DataSource dataSource, TeacherMapper teacherMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.teacherMapper = teacherMapper;
    }

    @Override
    public boolean addTeacher(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Adding {}", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_TEACHER, firstName, lastName) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding {}", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't add " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to add %s", teacherMessage);
            LOGGER.error(failMessage, e);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        LOGGER.debug("Getting Teacher by id '{}'", teacherId);
        Teacher teacher;
        try {
            teacher = jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, new Object[] { teacherId }, teacherMapper);
            LOGGER.info("Successfully retrived Teacher {}", teacher);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get Teacher by Id %d", teacherId);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return teacher;
    }

    @Override
    public Teacher getTeacherByName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Getting {}", teacherMessage);
        Teacher teacher;
        try {
            teacher = jdbcTemplate.queryForObject(GET_TEACHER_BY_NAME, new Object[] { firstName, lastName },
                    teacherMapper);
            LOGGER.info("Successfully retrived {}", teacherMessage);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get %s", teacherMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        LOGGER.debug("Getting all teachers");
        List<Teacher> allTeachers = new ArrayList<>();
        try {
            allTeachers.addAll(jdbcTemplate.query(GET_ALL_TEACHERS, teacherMapper));
            LOGGER.info("Successfully query for all teachers");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all teachers from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allTeachers;
    }

    @Override
    public boolean updateTeacher(String firstName, String lastName, long teacherId) {
        String teacherMessage = String.format("Teacher with ID = %d and firstName = %s, lastname = %s", teacherId,
                firstName, lastName);
        LOGGER.debug("Updating '{}'", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_TEACHER, firstName, lastName, teacherId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated '{}'", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't update " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to update %s", teacherMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteTeacherById(long teacherId) {
        String teacherMessage = String.format("Teacher by id %d", teacherId);
        LOGGER.debug("Deleting '{}'", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_TEACHER_BY_ID, teacherId) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting '{}'", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't delete " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failDeleteMessage = "Failed to delete " + teacherMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }
}
=======
package ua.com.nikiforov.dao.persons;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.FIRST_NAME;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.LAST_NAME;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.ID;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.TABLE_TEACHERS;

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
import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAOImpl.class);

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
    public TeacherDAOImpl(DataSource dataSource, TeacherMapper teacherMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.teacherMapper = teacherMapper;
    }

    @Override
    public boolean addTeacher(String firstName, String lastName) {
        String teacherMessage = String.format("Student with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Adding {}", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_TEACHER, firstName, lastName) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding {}", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't add " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to add %s", teacherMessage);
            LOGGER.error(failMessage, e);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        LOGGER.debug("Getting Student by id '{}'", teacherId);
        Teacher teacher;
        try {
            teacher = jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, new Object[] { teacherId }, teacherMapper);
            LOGGER.info("Successfully retrived Student {}", teacher);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get Student by Id %d", teacherId);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return teacher;
    }

    @Override
    public Teacher getTeacherByName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Getting {}", teacherMessage);
        Teacher teacher;
        try {
            teacher = jdbcTemplate.queryForObject(GET_TEACHER_BY_NAME, new Object[] { firstName, lastName },
                    teacherMapper);
            LOGGER.info("Successfully retrived {}", teacherMessage);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get %s", teacherMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        LOGGER.debug("Getting all teachers");
        List<Teacher> allTeachers = new ArrayList<>();
        try {
            allTeachers.addAll(jdbcTemplate.query(GET_ALL_TEACHERS, teacherMapper));
            LOGGER.info("Successfully query for all teachers");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all teachers from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allTeachers;
    }

    @Override
    public boolean updateTeacher(String firstName, String lastName, long teacherId) {
        String teacherMessage = String.format("Teacher with ID = %d and firstName = %s, lastname = %s", teacherId,
                firstName, lastName);
        LOGGER.debug("Updating '{}'", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_TEACHER, firstName, lastName, teacherId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated '{}'", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't update " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to update %s", teacherMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteTeacherById(long teacherId) {
        String teacherMessage = String.format("Teacher by id %d", teacherId);
        LOGGER.debug("Deleting '{}'", teacherMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_TEACHER_BY_ID, teacherId) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting '{}'", teacherMessage);
            } else {
                throw new DataOperationException("Couldn't delete " + teacherMessage);
            }
        } catch (DataAccessException e) {
            String failDeleteMessage = "Failed to delete " + teacherMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }
}
>>>>>>> refs/remotes/origin/master
