package ua.com.nikiforov.dao.subject;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.SubjectTable.*;

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
import ua.com.nikiforov.mappers.SubjectMapper;
import ua.com.nikiforov.models.Subject;

@Repository
public class SubjectDAOImpl implements SubjectDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDAOImpl.class);

    private static final String ADD_SUBJECT = "INSERT INTO subjects  (subject_name) VALUES(?)";
    private static final String GET_SUBJECT_BY_ID = SELECT + ASTERISK + FROM + TABLE_SUBJECTS + WHERE + SUBJECTS_SUBJECT_ID
            + EQUALS_M + Q_MARK;
    private static final String GET_SUBJECT_BY_NAME = SELECT + ASTERISK + FROM + TABLE_SUBJECTS + WHERE + SUBJECTS_SUBJECT_NAME
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_SUBJECTS = SELECT + ASTERISK + FROM + TABLE_SUBJECTS;
    private static final String UPDATE_SUBJECT = UPDATE + TABLE_SUBJECTS + SET + SUBJECTS_SUBJECT_NAME + EQUALS_M + Q_MARK
            + WHERE + SUBJECTS_SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_SUBJECT_BY_ID = DELETE + FROM + TABLE_SUBJECTS + WHERE + SUBJECTS_SUBJECT_ID + EQUALS_M
            + Q_MARK;

    private SubjectMapper subjectMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectDAOImpl(DataSource dataSource, SubjectMapper subjectMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.subjectMapper = subjectMapper;
    }

    @Override
    public boolean addSubject(String subjectName) {
        String subjectMessage = String.format("Subject with name %s", subjectName);
        LOGGER.debug("Adding  '{}'", subjectName);
        boolean actionResult = jdbcTemplate.update(ADD_SUBJECT, subjectName) > 0;
        try {
            if (actionResult) {
                LOGGER.info("Successful adding '{}' ", subjectMessage);
            } else {
                String failMessage = String.format("Fail to add %s", subjectMessage);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String message = String.format("Couldn't add %s", subjectMessage);
            LOGGER.error(message);
            throw new DataOperationException(message, e);
        }
        return true;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        String subjectMessage = String.format("Subject by id %d", subjectId);
        LOGGER.debug("Getting '{}'", subjectMessage);
        Subject subject;
        try {
            subject = jdbcTemplate.queryForObject(GET_SUBJECT_BY_ID, new Object[] { subjectId }, subjectMapper);
            LOGGER.info("Successfully retrieved '{}'", subject);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Fail to get %s", subjectMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage, e);
        }
        return subject;
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        String subjectMessage = String.format("Subject by name %s", subjectName);
        LOGGER.debug("Getting '{}'", subjectMessage);
        Subject subject;
        try {
            subject = jdbcTemplate.queryForObject(GET_SUBJECT_BY_NAME, new Object[] { subjectName }, subjectMapper);
            LOGGER.info("Successfully retrieved '{}'", subject);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Fail to get %s", subjectMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage, e);
        }
        return subject;
    }

    @Override
    public List<Subject> getAllSubjects() {
        LOGGER.debug("Getting all Subjects. ");
        List<Subject> allSubjects = new ArrayList<>();
        try {
            allSubjects.addAll(jdbcTemplate.query(GET_ALL_SUBJECTS, subjectMapper));
            LOGGER.info("Successfully query for all subjects");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all subjects from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allSubjects;
    }

    @Override
    public boolean updateSubject(String subjectName, int subjectId) {
        String updateMessage = String.format("Subject with name %s by id %d", subjectName, subjectId);
        LOGGER.debug("Updating {}", updateMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_SUBJECT, subjectName, subjectId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated '{}'", updateMessage);
            } else {
                String failMessage = String.format("Couldn't update %s", updateMessage);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't update %s", updateMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteSubjectById(int subjectId) {
        String deleteMessage = String.format("Subject by id %d", subjectId);
        LOGGER.debug("Deleting {}", deleteMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_SUBJECT_BY_ID, subjectId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully deleted '{}'", deleteMessage);
            } else {
                String failMessage = String.format("Couldn't delete %s", deleteMessage);
                throw new EntityNotFoundException(failMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

}
