package ua.com.nikiforov.dao.teachers_subjects;

import static ua.com.nikiforov.dao.SqlConstants.*;

import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.*;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.*;
import static ua.com.nikiforov.dao.SqlConstants.SubjectTable.*;

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
import ua.com.nikiforov.mappers.SubjectMapper;
import ua.com.nikiforov.mappers.TeachersSubjectsMapper;
import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.TeachersSubjects;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeachersSubjectsDAOImpl implements TeachersSubjectsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersSubjectsDAOImpl.class);

    private static final String GET_SUBJECTS_IDS = SELECT + ASTERISK + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + TEACHERS_SUBJECTS_TEACHER_ID + EQUALS_M + Q_MARK;
    private static final String ADD_SUBJECT_FOR_TEACHER = "INSERT INTO teachers_subjects (teacher_id,subject_id) VALUES(?,?)";
    private static final String DELETE_SUBJECT_FROM_TEACHER = DELETE + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + TEACHERS_SUBJECTS_TEACHER_ID + EQUALS_M + Q_MARK + AND + TEACHERS_SUBJECTS_SUBJECT_ID + EQUALS_M + Q_MARK;

    private static final String GET_TEACHERS_BY_SUBJECT_ID = SELECT + TEACHERS_TEACHER_ID + COMA + TEACHERS_FIRST_NAME
            + COMA + TEACHERS_LAST_NAME + FROM + TABLE_SUBJECTS + INNER + JOIN + TEACHERS_SUBJECTS_TABLE + ON
            + SUBJECTS_SUBJECT_ID + EQUALS_M + TEACHERS_SUBJECTS_SUBJECT_ID + INNER + JOIN + TABLE_TEACHERS + ON
            + TEACHERS_SUBJECTS_TEACHER_ID + EQUALS_M + TEACHERS_TEACHER_ID + WHERE + SUBJECTS_SUBJECT_ID + EQUALS_M
            + Q_MARK;

    private static final String GET_SUBJECTS_BY_TEACHER_ID = SELECT + SUBJECTS_SUBJECT_ID + COMA + SUBJECTS_SUBJECT_NAME + FROM
            + TABLE_TEACHERS + INNER + JOIN + TEACHERS_SUBJECTS_TABLE + ON + TEACHERS_TEACHER_ID + EQUALS_M
            + TEACHERS_SUBJECTS_TEACHER_ID + INNER + JOIN + TABLE_SUBJECTS + ON + TEACHERS_SUBJECTS_SUBJECT_ID
            + EQUALS_M + SUBJECTS_SUBJECT_ID + WHERE + TEACHERS_TEACHER_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;
    private SubjectMapper subjectMapper;

    @Autowired
    public TeachersSubjectsDAOImpl(DataSource dataSource, SubjectMapper subjectMapper, TeacherMapper teacherMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> getTeachers(int subjectId) {
        String subjectsTeacherIdsMSG = String.format("Subjects Teachers by subjectId %d", subjectId);
        LOGGER.debug("Getting {}", subjectsTeacherIdsMSG);
        List<Teacher> teachers = new ArrayList<>();
        try {
            teachers.addAll(jdbcTemplate.query(GET_TEACHERS_BY_SUBJECT_ID, new Object[] { subjectId }, teacherMapper));
            LOGGER.info("Got {}", subjectsTeacherIdsMSG);
        } catch (DataAccessException e) {
            throw new DataOperationException("Couldn't get " + subjectsTeacherIdsMSG, e);
        }
        return teachers;
    }

    @Override
    public List<Subject> getSubjects(long teacherId) {
        String teachersSubjectIdsMSG = String.format("Teacher Subjects by teacherIds %d", teacherId);
        LOGGER.debug("Getting {}", teachersSubjectIdsMSG);
        List<Subject> subjects = new ArrayList<>();
        try {
            subjects.addAll(jdbcTemplate.query(GET_SUBJECTS_BY_TEACHER_ID, new Object[] { teacherId }, subjectMapper));
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to get %s", teachersSubjectIdsMSG);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return subjects;
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
