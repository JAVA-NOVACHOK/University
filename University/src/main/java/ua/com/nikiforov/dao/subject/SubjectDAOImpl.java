package ua.com.nikiforov.dao.subject;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Subject;

@Repository
public class SubjectDAOImpl implements SubjectDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDAOImpl.class);

    private static final String GET_SUBJECT_BY_NAME = "SELECT s FROM Subject s WHERE s.name =  ?1";
    private static final String GET_ALL_SUBJECTS = "SELECT  s  FROM Subject s ORDER BY s.name";
    private static final String DELETE_SUBJECT_BY_ID = "DELETE  FROM Subject s WHERE s.id =  ?1";

    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIEVED_MSG = "Successfully retrived {}";

    private static final int FIRST_PARAMETER_INDEX = 1;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addSubject(String subjectName) {
        Subject subject = new Subject();
        subject.setName(subjectName);
        entityManager.persist(subject);
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        String subjectMessage = String.format("Subject by id %d", subjectId);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        Subject subject;
        subject = entityManager.find(Subject.class, subjectId);
        if (subject == null) {
            String failMessage = String.format("Fail to get subject by Id %d from DB", subjectId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, subject);
        return subject;
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        String subjectMessage = String.format("Subject by name %s", subjectName);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        Subject subject = (Subject) entityManager.createQuery(GET_SUBJECT_BY_NAME)
                .setParameter(FIRST_PARAMETER_INDEX, subjectName)
                .getSingleResult();
        if (subject == null) {
            String failMessage = String.format("Fail to get subject by name %s from DB", subjectName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, subject);
        return subject;
    }

    @Override
    public List<Subject> getAllSubjects() {
        LOGGER.debug("Getting all Subjects.");
        List<Subject> allSubjects = new ArrayList<>();
        try {
            allSubjects.addAll(entityManager.createQuery(GET_ALL_SUBJECTS, Subject.class).getResultList());
            LOGGER.info("Successfully query for all subjects");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all subjects from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allSubjects;
    }

    @Override
    @Transactional
    public void updateSubject(SubjectDTO subjectDTO) {
        int subjectId = subjectDTO.getId();
        String subjectName = subjectDTO.getName();
        Subject subject = entityManager.find(Subject.class, subjectId);
        String updateMessage = String.format("Subject with name %s by id %d", subject.getName(), subject.getId());
        LOGGER.debug("Updating {}", updateMessage);
        try {
            entityManager.merge(new Subject(subjectId, subjectName, subject.getTeachers()));
            LOGGER.info("Successfully updated '{}'", updateMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Subject with id %d name %s from DAO %s", subjectId, subjectName,e);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    @Transactional
    public void deleteSubjectById(int subjectId) {
        String deleteMessage = String.format("Subject by id %d", subjectId);
        LOGGER.debug("Deleting {}", deleteMessage);
        try {
            entityManager.createQuery(DELETE_SUBJECT_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, subjectId)
                    .executeUpdate();
            LOGGER.info("Successfully deleted '{}'", deleteMessage);
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

}
