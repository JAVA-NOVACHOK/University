package ua.com.nikiforov.services.university;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.repositories.university.UniversityRepository;
import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.UniversityMapperDTO;
import ua.com.nikiforov.models.University;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class UniversityServiceImpl implements UniversityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversityServiceImpl.class);

    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIVED_MSG = "Successfully retrived {}";

    private UniversityRepository universityRepository;
    private UniversityMapperDTO universityMapper;

    @Autowired
    public UniversityServiceImpl(UniversityRepository universityRepository, UniversityMapperDTO universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    @Override
    @Transactional
    public UniversityDTO addUniversity(String name) {
        String universityMessage = String.format("University with name %s", name);
        LOGGER.debug("Adding {}", universityMessage);
        UniversityDTO universityDTO;
        try {
           universityDTO = universityMapper.universityToUniversityDTO(universityRepository.save(new University(name)));
            LOGGER.info("Successfully added {}", universityMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("University is already exists", e);
        }
        return universityDTO;
    }

    @Override
    public UniversityDTO getUniversityById(int id) {
        String universityMessage = String.format("University with ID %d", id);
        LOGGER.debug(GETTING_MSG, universityMessage);
        UniversityDTO university;
        try {
            university = universityMapper.universityToUniversityDTO(universityRepository.getOne(id));
            LOGGER.info("Successfully retrieved University '{}'", universityMessage);
        } catch (NoResultException e) {
            String failMessage = String.format("Fail to get room by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        return university;
    }

    public UniversityDTO getUniversityByName(String universityName) {
        String subjectMessage = String.format("University by name %s", universityName);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        UniversityDTO university;
        try {
            university = universityMapper.universityToUniversityDTO(universityRepository.getUniversityByUniversityName(universityName));
        } catch (NoResultException e) {
            String failMessage = String.format("Fail to get university by name %s from DB", universityName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIVED_MSG, university);
        return university;
    }


    @Override
    public void updateUniversity(UniversityDTO universityDTO) {
       University university = universityMapper.universityDTOToUniversity(universityDTO);
        String updateMessage = String.format("University with name %s by id %d", university.getName(), university.getId());
        LOGGER.debug("Updating {}", updateMessage);
        try {
            universityRepository.save(university);
            LOGGER.info("Successfully updated '{}'", updateMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update University with id %d name %s from DAO %s", university.getId(), university.getName(), e);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    public void deleteUniversityById(int id) {
        String deleteMessage = String.format("University by id %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        try {
            universityRepository.deleteById(id);
            LOGGER.info("Successfully deleted '{}'", deleteMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }

    }

}
