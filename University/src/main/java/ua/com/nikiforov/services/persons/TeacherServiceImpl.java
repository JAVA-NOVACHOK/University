package ua.com.nikiforov.services.persons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.repositories.persons.TeacherRepository;
import ua.com.nikiforov.repositories.subject.SubjectRepository;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.TeacherMapperDTO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private static final Sort SORT_BY_LAST_NAME = Sort.by(Sort.Direction.ASC,"lastName");

    private static final String GETTING_MSG = "Getting {}";

    private TeacherRepository teacherRepository;
    private SubjectRepository subjectRepository;
    private TeacherMapperDTO teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              TeacherMapperDTO teacherMapper,
                              SubjectRepository subjectRepository) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void addTeacher(TeacherDTO teacherDTO) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastName = %s",
                teacherDTO.getFirstName(), teacherDTO.getLastName());
        LOGGER.debug("Adding {}", teacherMessage);
        try {
            teacherRepository.save(teacherMapper.teacherDTOToTeacher(teacherDTO));
            LOGGER.info("Successful added {}", teacherMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + teacherMessage, e);
        }
    }

    @Override
    @Transactional
    public TeacherDTO getTeacherById(long teacherId) {
        String getMessage = String.format("Teacher by id %d", teacherId);
        LOGGER.debug(GETTING_MSG, getMessage);
        Teacher teacher;
        try {
            teacher = teacherRepository.findById(teacherId).orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            String failMessage = String.format("Failed to get %s", getMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved {}", getMessage);
        return teacherMapper.getTeacherDTO(teacher);
    }

    @Override
    @Transactional
    public TeacherDTO getTeacherByName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug(GETTING_MSG, teacherMessage);
        TeacherDTO teacher;
        try {
            teacher = teacherMapper.getTeacherDTO(teacherRepository.getTeacherByFirstNameAndLastName(firstName, lastName));
        } catch (NoResultException e) {
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            LOGGER.debug(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage);
        }
        LOGGER.info("Successfully retrieved {}", teacherMessage);
        return teacher;
    }

    @Override
    @Transactional
    public List<TeacherDTO> getAllTeachers() {
        String teachersMSG = "all teachers";
        LOGGER.debug(GETTING_MSG, teachersMSG);
        List<TeacherDTO> allTeachers = new ArrayList<>();
        try {
            allTeachers.addAll(teacherMapper.getTeacherDTOList(teacherRepository.findAll(SORT_BY_LAST_NAME)));
            LOGGER.info("Successfully query for {}", teachersMSG);
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
        Teacher teacherWithSubjects = teacherRepository.getOne(newTeacher.getId());
        newTeacher.setSubjects(teacherWithSubjects.getSubjects());
        String teacherMessage = String.format("Teacher with ID = %d and firstName = %s, lastname = %s", newTeacher.getId(),
                newTeacher.getFirstName(), newTeacher.getLastName());
        LOGGER.debug("Updating '{}'", teacherMessage);
        try {
            teacherRepository.save(newTeacher);
            LOGGER.info("Successfully updated '{}'", teacherMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException(teacherMessage + " already exists", e);
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
            teacherRepository.deleteById(teacherId);
            LOGGER.info("Successful deleting '{}'", teacherMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = "Failed to delete " + teacherMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }

    }

    @Override
    @Transactional
    public TeacherDTO assignSubjectToTeacher(long teacherId, int subjectId) {
        LOGGER.debug("Assigning subject with id %d to teacher with ");
        Teacher teacher = teacherRepository.getOne(teacherId);
        Subject subject = subjectRepository.getOne(subjectId);
        teacher.getSubjects().add(subject);
        return teacherMapper.getTeacherDTO(teacherRepository.save(teacher));
    }

    @Override
    @Transactional
    public TeacherDTO unassignSubjectFromTeacher(long teacherId, int subjectId) {
        LOGGER.debug("Unassigning subject with id %d to teacher with ");
        Teacher teacher = teacherRepository.getOne(teacherId);
        Subject subject = subjectRepository.getOne(subjectId);
        teacher.getSubjects().remove(subject);
        return teacherMapper.getTeacherDTO(teacherRepository.save(teacher));
    }

    @Override
    @Transactional
    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with searching parameters firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug(GETTING_MSG, teacherMessage);
        List<TeacherDTO> teachers = new ArrayList<>();
        try {
            teachers.addAll(teacherMapper.getTeacherDTOList(teacherRepository.getTeacherByLikeName(firstName, lastName)));
        } catch (PersistenceException e) {
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            throw new DataOperationException(failGetByIdMessage, e);
        }
        return teachers;
    }

}
