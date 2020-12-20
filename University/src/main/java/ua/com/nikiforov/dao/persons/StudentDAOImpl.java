package ua.com.nikiforov.dao.persons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.persons.StudentMapper;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

@Repository
public class StudentDAOImpl implements StudentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAOImpl.class);

    private static final String FIND_STUDENT_BY_NAME_GROUP_ID = "SELECT s FROM Student s JOIN s.group g WHERE g.groupId = ?1 AND s.firstName = ?2 AND s.lastName = ?3";
    private static final String FIND_STUDENT_BY_NAME = "SELECT s FROM Student s WHERE s.firstName LIKE '%?1' AND s.lastName LIKE '%?2'";
    private static final String GET_ALL_STUDENTS = "SELECT s FROM Student s ORDER BY s.lastName";
    private static final String DELETE_STUDENT_BY_ID = "DELETE FROM Student s WHERE s.id = ?1";

    private static final String GETTING = "Getting {}";
    private static final String SUCCESSFULLY_RETRIEVED_STUDENT = "Successfully retrieved student '{}'";

    private static final int FIRST_PARAMETER_INDEX = 1;
    private static final int SECOND_PARAMETER_INDEX = 2;
    private static final int THIRED_PARAMETER_INDEX = 3;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void addStudent(StudentDTO studentDTO) {
        String firstName = studentDTO.getFirstName();
        String lastName = studentDTO.getLastName();
        long groupId = studentDTO.getGroupId();
        Group group = entityManager.getReference(Group.class, groupId);
        String studentMessage = String.format("Student with firstName = %s, lastname = %s, groupId = %d", firstName,
                lastName, groupId);
        LOGGER.debug("Adding {}", studentMessage);
        entityManager.persist(new Student(firstName, lastName, group));
        LOGGER.debug("{} added successfully", studentMessage);
    }

    @Override
    public Student getStudentById(long studentId) {
        LOGGER.debug("Getting Student by id '{}'", studentId);
        Student student;
        student = entityManager.find(Student.class, studentId);
        if (student == null) {
            String failMessage = String.format("Fail to get student by Id %d from DB", studentId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return student;
    }

    @Override
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        String studentMessage = String.format("Student with firstName = %s, lastname = %s, groupId = %d", firstName,
                lastName, groupId);
        LOGGER.debug(GETTING, studentMessage);
        Student student;
        student = (Student) entityManager.createQuery(FIND_STUDENT_BY_NAME_GROUP_ID)
                .setParameter(FIRST_PARAMETER_INDEX, groupId)
                .setParameter(SECOND_PARAMETER_INDEX, firstName)
                .setParameter(THIRED_PARAMETER_INDEX, lastName)
                .getSingleResult();
        if (student == null) {
            String failMessage = String.format("Fail to get student by groupId %d, firstName = %s and lastName = %s from DB", groupId, firstName, lastName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return student;
    }

    @Override
    public Student getStudentByName(String firstName, String lastName) {
        String studentMessage = String.format("Student with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug(GETTING, studentMessage);
        Student student;
        student = (Student) entityManager.createQuery(FIND_STUDENT_BY_NAME)
                .setParameter(FIRST_PARAMETER_INDEX, firstName)
                .setParameter(SECOND_PARAMETER_INDEX, lastName)
                .getSingleResult();
        if (student == null) {
            String failMessage = String.format("Fail to get student by firstName = %s and lastName = %s from DB", firstName, lastName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        LOGGER.debug("Getting all students");
        List<Student> allStudents = new ArrayList<>();
        try {
            allStudents.addAll(entityManager.createQuery(GET_ALL_STUDENTS, Student.class).getResultList());
            LOGGER.info("Successfully query for all students");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all students from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        Collections.sort(allStudents);
        return allStudents;
    }

    @Override
    @Transactional
    public void updateStudent(StudentDTO student) {
        long studentId = student.getId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        Group group = entityManager.getReference(Group.class, student.getGroupId());
        LOGGER.debug("Updating {}", student);
        try {
            entityManager.merge(new Student(studentId, firstName, lastName, group));
        } catch (PersistenceException e) {
            String failMessage = String.format("Failed to update %s", student);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    @Transactional
    public void deleteStudentById(long studentId) {
        String studentMessage = String.format("Student by id %d", studentId);
        LOGGER.debug("Deleting {}", studentMessage);
        try {
            entityManager.createQuery(DELETE_STUDENT_BY_ID)
                    .setParameter(1, studentId)
                    .executeUpdate();
            LOGGER.info("Successful deleting {}", studentMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = "Failed to delete " + studentMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }
}
