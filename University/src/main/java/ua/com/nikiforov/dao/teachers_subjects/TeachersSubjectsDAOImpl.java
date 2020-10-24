package ua.com.nikiforov.dao.teachers_subjects;

import static ua.com.nikiforov.dao.SqlConstants.*;

import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.SUBJECT_ID;
import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.TEACHERS_SUBJECTS_TABLE;
import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.TEACHER_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers.TeachersSubjectsMapper;
import ua.com.nikiforov.models.TeachersSubjects;

@Repository
public class TeachersSubjectsDAOImpl implements TeachersSubjectsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersSubjectsDAOImpl.class);

    private static final String GET_TEACHERS_IDS = SELECT + ASTERISK + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String GET_SUBJECTS_IDS = SELECT + ASTERISK + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + TEACHER_ID + EQUALS_M + Q_MARK;
    private static final String ADD_SUBJECT_FOR_TEACHER = INSERT + TEACHERS_SUBJECTS_TABLE + SET + TEACHER_ID + EQUALS_M
            + Q_MARK + COMA + SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_SUBJECT_FROM_TEACHER = DELETE + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + TEACHER_ID + EQUALS_M + Q_MARK + AND + SUBJECT_ID + EQUALS_M + Q_MARK;

    private static final int PREPARE_STATEMENT_FIRST_INDEX = 1;

    private TeachersSubjectsMapper teachersSubjectsMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeachersSubjectsDAOImpl(DataSource dataSource, TeachersSubjectsMapper teachersSubjectsMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.teachersSubjectsMapper = teachersSubjectsMapper;
    }

    @Override
    public List<Long> getTeachersIds(int subjectId) {
        String subjectsTeacherIdsMSG = String.format("Subjects teacherIds by subjectId %d", subjectId);
        LOGGER.debug("Getting {}", subjectsTeacherIdsMSG);
        List<TeachersSubjects> teachersSubjects = new ArrayList<>();
        try {
            teachersSubjects
                    .addAll(jdbcTemplate.query(GET_TEACHERS_IDS, new Object[] { subjectId }, teachersSubjectsMapper));
            LOGGER.info("Got {}", subjectsTeacherIdsMSG);
        } catch (DataAccessException e) {
            throw new DataOperationException("Couldn't get " + subjectsTeacherIdsMSG);
        }
        return teachersSubjects.stream().map(TeachersSubjects::getTeachersId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSubjectsIds(long teacherId) {
        String teachersSubjectIdsMSG = String.format("Teachers subjectIds by teacherIds %d", teacherId);
        LOGGER.debug("Getting {}", teachersSubjectIdsMSG);
        List<TeachersSubjects> teachersSubjects = new ArrayList<>();
        try {
            teachersSubjects.addAll(jdbcTemplate.query(GET_SUBJECTS_IDS,
                    ps -> ps.setLong(PREPARE_STATEMENT_FIRST_INDEX, teacherId), teachersSubjectsMapper));
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to get %s", teachersSubjectIdsMSG);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return teachersSubjects.stream().map(TeachersSubjects::getSubjectId).collect(Collectors.toList());
    }

    @Override
    public boolean assignSubjectToTeacher(long teacherId, int subjectId) {
        String assignSubjectMessage = String.format("subject with id = %d to teacher with id = %d", subjectId,
                teacherId);
        LOGGER.debug("Assigning {}", assignSubjectMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_SUBJECT_FOR_TEACHER, teacherId, subjectId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully assigned {}", assignSubjectMessage);
            } else {
                throw new DataOperationException("Couldn't assign " + assignSubjectMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to assign %s", assignSubjectMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return actionResult;
    }

    @Override
    public boolean unassignSubjectFromTeacher(long teacherId, int subjectId) {
        String assignSubjectMessage = String.format("subject with id = %d from teacher with id = %d", subjectId,
                teacherId);
        LOGGER.debug("Unassigning {}", assignSubjectMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_SUBJECT_FROM_TEACHER, teacherId, subjectId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully unassigned {}", assignSubjectMessage);
            } else {
                throw new DataOperationException("Couldn't unassign " + assignSubjectMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to unassign %s", assignSubjectMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return actionResult;
    }

}
