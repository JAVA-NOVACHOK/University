package ua.com.nikiforov.services.subject;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.repositories.subject.SubjectRepository;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.SubjectMapperDTO;
import ua.com.nikiforov.models.Subject;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private static final Sort SORT_BY_SUBJECT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private static final String GETTING_MSG = "Getting '{}'";
    private static final String SUCCESSFULLY_RETRIEVED_MSG = "Successfully retrieved {}";

    private SubjectRepository subjectRepository;
    private SubjectMapperDTO subjectMapper;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubjectMapperDTO subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void addSubject(String subjectName) {
        String addMessage = String.format("subject with name %s", subjectName);
        LOGGER.debug("Adding '{}'", addMessage);
        try {
            subjectRepository.save(new Subject(subjectName));
            LOGGER.debug("Successfully added '{}'", addMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + addMessage, e);
        }
    }

    @Override
    @Transactional
    public SubjectDTO getSubjectById(int subjectId) {
        String subjectMessage = String.format("Subject by id %d", subjectId);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        Subject subject;
        try {
            subject = subjectRepository.findById(subjectId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format("Fail to get subject by Id %d from DB", subjectId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, subject);
        return subjectMapper.subjectToSubjectDTO(subject);

    }

    @Override
    @Transactional
    public SubjectDTO getSubjectByName(String subjectName) {
        String subjectMessage = String.format("Subject by name %s", subjectName);
        LOGGER.debug(GETTING_MSG, subjectMessage);
        Subject subject;
        try {
            subject = subjectRepository.getSubjectByName(subjectName).orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e) {
            String failMessage = String.format("Fail to get subject by name %s from DB", subjectName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_MSG, subject);
        return subjectMapper.subjectToSubjectDTO(subject);

    }

    @Override
    @Transactional
    public List<SubjectDTO> getAllSubjects() {
        LOGGER.debug("Getting all Subjects.");
        List<SubjectDTO> allSubjects = new ArrayList<>();
        try {
            allSubjects.addAll(subjectMapper.getSubjectDTOList(subjectRepository.findAll(SORT_BY_SUBJECT_NAME)));
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
        Subject newSubject = subjectMapper.subjectDTOToSubject(subjectDTO);
        Subject subjectWithTeachers = subjectMapper.subjectDTOToSubject(getSubjectById(subjectDTO.getId()));
        newSubject.setTeachers(subjectWithTeachers.getTeachers());
        String updateMessage = String.format("Subject with name %s by id %d", newSubject.getName(), newSubject.getId());
        LOGGER.debug("Updating {}", updateMessage);
        try {
            subjectRepository.save(newSubject);
            LOGGER.info("Successfully updated '{}'", updateMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while editing!", e);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Subject with id %d name %s from DAO %s", subjectDTO.getId(), subjectDTO.getName(), e);
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
            subjectRepository.deleteById(subjectId);
            LOGGER.info("Successfully deleted '{}'", deleteMessage);
        } catch (EmptyResultDataAccessException e) {
            throw new DataOperationException("Something went wrong!", e);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        subjectRepository.deleteById(subjectId);
    }

}
