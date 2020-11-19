package ua.com.nikiforov.dao.teachers_subjects;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers.SubjectMapper;
import ua.com.nikiforov.mappers.persons.TeacherMapper;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeachersSubjectsDAOImpl implements TeachersSubjectsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersSubjectsDAOImpl.class);

    private static final String GET_SUBJECTS_IDS = "SELECT  *  FROM teachers_subjects WHERE teachers_subjects.teacher_id =  ? ";
    private static final String ADD_SUBJECT_FOR_TEACHER = "INSERT INTO teachers_subjects (teacher_id,subject_id) VALUES(?,?)";
    private static final String DELETE_SUBJECT_FROM_TEACHER = "DELETE  FROM teachers_subjects WHERE teachers_subjects.teacher_id =  ?  AND teachers_subjects.subject_id =  ? ";
    private static final String GET_TEACHERS_BY_SUBJECT_ID = "SELECT teachers.teacher_id,teachers.first_name,teachers.last_name FROM subjects"
                + "  INNER  JOIN teachers_subjects "
                + "ON subjects.subject_id = teachers_subjects.subject_id "
                + "INNER  JOIN teachers "
                + "ON teachers_subjects.teacher_id = teachers.teacher_id "
                + "WHERE subjects.subject_id =  ? ";

    private static final String GET_SUBJECTS_BY_TEACHER_ID = "SELECT subjects.subject_id,subjects.subject_name "
            + "FROM teachers INNER  JOIN teachers_subjects "
            + "ON teachers.teacher_id = teachers_subjects.teacher_id "
            + "INNER  JOIN subjects  "
            + "ON teachers_subjects.subject_id = subjects.subject_id "
            + "WHERE teachers.teacher_id =  ? ";
            

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
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Already assined " + assignSubjectMessage,e);
        }catch (DataAccessException e) {
            String failMessage = String.format("Failed to assign %s", assignSubjectMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
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
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

}
