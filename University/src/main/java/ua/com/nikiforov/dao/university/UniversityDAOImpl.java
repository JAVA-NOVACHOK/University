package ua.com.nikiforov.dao.university;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.University;

@Repository
public class UniversityDAOImpl implements UniversityDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversityDAOImpl.class);

    private static final String FIND_UNIVERSITY_BY_NAME = "SELECT  u  FROM University u WHERE u.universityName =  ?1";
    private static final String DELETE_UNIVERSITY_BY_ID = "DELETE  FROM University u WHERE u.id =  ?1";

    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIVED_MSG = "Successfully retrived {}";

    private static final int FIRST_PARAMETER_INDEX = 1;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUniversity(String name) {
        String universityMessage = String.format("University with name %s", name);
        LOGGER.debug("Adding {}", universityMessage);
            entityManager.persist(new University(name));
                LOGGER.info("Successfully added {}", universityMessage);
    }

    @Override
    public University findUniversityById(int id) {
        String universityMessage = String.format("University with ID %d", id);
        LOGGER.debug(GETTING_MSG, universityMessage);
        University university = entityManager.find(University.class,id);
            LOGGER.info("Successfully retrieved University '{}'", universityMessage);
       if(university == null){
            String failMessage = String.format("Fail to get room by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        return university;
    }

    @Override
    public University getUniversityByName(String universityName) {
        String subjectMessage = String.format("University by name %s", universityName);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        University university = (University) entityManager.createQuery(FIND_UNIVERSITY_BY_NAME)
                .setParameter(FIRST_PARAMETER_INDEX, universityName)
                .getSingleResult();
        if (university == null) {
            String failMessage = String.format("Fail to get university by name %s from DB", university);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIVED_MSG, university);
        return university;
    }


    @Override
    public void updateUniversity(UniversityDTO universityDTO) {
        int id = universityDTO.getId();
        String universityName = universityDTO.getUniversityName();
        String updateMessage = String.format("University with name %s by id %d", universityName, id);
        LOGGER.debug("Updating {}", updateMessage);
        try {
            entityManager.merge(new University(id, universityName));
            LOGGER.info("Successfully updated '{}'", updateMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update University with id %d name %s from DAO %s", id, universityName,e);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    public void deleteUniversityById(int id) {
        String deleteMessage = String.format("University by id %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        try {
            entityManager.createQuery(DELETE_UNIVERSITY_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, id)
                    .executeUpdate();
            LOGGER.info("Successfully deleted '{}'", deleteMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }
}
