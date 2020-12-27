package ua.com.nikiforov.dao.persons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.TeacherMapperDTO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAOImpl.class);

    private static final String GET_TEACHER_BY_NAME = "SELECT t FROM Teacher t WHERE t.firstName = ?1 AND t.lastName = ?2";
    private static final String GET_TEACHER_BY_LIKE_NAME = "SELECT t FROM Teacher t WHERE UPPER(t.firstName) LIKE UPPER( CONCAT(?1,'%')) " +
            "OR UPPER(t.lastName) LIKE UPPER( CONCAT(?2,'%')) ORDER BY t.lastName";
    private static final String GET_ALL_TEACHERS = "SELECT t FROM Teacher t ORDER BY t.firstName";
    private static final String DELETE_TEACHER_BY_ID = "DELETE FROM Teacher t WHERE t.id = ?1";

    private static final int FIRST_PARAMETER_INDEX = 1;
    private static final int SECOND_PARAMETER_INDEX = 2;

    private TeacherMapperDTO teacherMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TeacherDAOImpl(TeacherMapperDTO teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional
    public void addTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        String teacherMessage = String.format("Teacher with firstName = %s, lastName = %s", teacher.getFirstName(), teacher.getLastName());
        LOGGER.debug("Adding {}", teacherMessage);
        entityManager.persist(teacher);
        LOGGER.info("Successful adding {}", teacherMessage);
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        LOGGER.debug("Getting Teacher by id '{}'", teacherId);
        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        if(teacher == null){
            String failMessage = String.format("Failed to get Teacher by id %d", teacherId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrived Teacher {}", teacher);
        return teacher;
    }

    @Override
    public Teacher getTeacherByName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Getting {}", teacherMessage);
        Teacher teacher;
        try {
            teacher = (Teacher) entityManager.createQuery(GET_TEACHER_BY_NAME)
                    .setParameter(FIRST_PARAMETER_INDEX, firstName)
                    .setParameter(SECOND_PARAMETER_INDEX, lastName)
                    .getSingleResult();
        }catch (NoResultException e){
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            LOGGER.debug(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage,e);
        }
        LOGGER.info("Successfully retrived {}", teacherMessage);
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        LOGGER.debug("Getting all teachers");
        List<Teacher> allTeachers = new ArrayList<>();
        try {
            allTeachers.addAll(entityManager.createQuery(GET_ALL_TEACHERS, Teacher.class).getResultList());
            Collections.sort(allTeachers);
            LOGGER.info("Successfully query for all teachers");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all teachers from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allTeachers;
    }

    @Override
    @Transactional
    public void updateTeacher(TeacherDTO teacherDTO) {
        Teacher newTeacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        Teacher teacherWithSubjects = entityManager.find(Teacher.class, newTeacher.getId());
        newTeacher.setSubjects(teacherWithSubjects.getSubjects());
        String teacherMessage = String.format("Teacher with ID = %d and firstName = %s, lastname = %s", newTeacher.getId(),
                newTeacher.getFirstName(), newTeacher.getLastName());
        LOGGER.debug("Updating '{}'", teacherMessage);
        try {
            entityManager.merge(newTeacher);
            LOGGER.info("Successfully updated '{}'", teacherMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("%s already exists", e);
        } catch (PersistenceException e) {
            String failMessage = String.format("Failed to update %s", teacherMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    @Transactional
    public void deleteTeacherById(long teacherId) {
        String teacherMessage = String.format("Teacher by id %d", teacherId);
        LOGGER.debug("Deleting '{}'", teacherMessage);
        try {
            entityManager.createQuery(DELETE_TEACHER_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, teacherId)
                    .executeUpdate();
            LOGGER.info("Successful deleting '{}'", teacherMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = "Failed to delete " + teacherMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }

    @Override
    @Transactional
    public void assignSubjectToTeacher(long teacherId, int subjectId) {
        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        Subject subject = entityManager.find(Subject.class, subjectId);
        teacher.getSubjects().add(subject);
        entityManager.merge(teacher);
    }

    @Override
    @Transactional
    public void unassignSubjectFromTeacher(long teacherId, int subjectId) {
        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        Subject subject = entityManager.find(Subject.class, subjectId);
        teacher.getSubjects().remove(subject);
        entityManager.merge(teacher);
    }

    @Override
    public List<Teacher> getTeacherByLikeName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with searching parameters firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug("Getting {}", teacherMessage);
        List<Teacher> teachers = null;
        try {
            teachers = entityManager.createQuery(GET_TEACHER_BY_LIKE_NAME, Teacher.class)
                    .setParameter(FIRST_PARAMETER_INDEX, firstName)
                    .setParameter(SECOND_PARAMETER_INDEX, lastName)
                    .getResultList();
        } catch (PersistenceException e) {
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            throw new DataOperationException(failGetByIdMessage, e);
        }
        return teachers;
    }

}
