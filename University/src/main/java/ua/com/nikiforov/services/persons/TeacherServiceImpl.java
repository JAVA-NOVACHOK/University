package ua.com.nikiforov.services.persons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.TeacherMapperDTO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private static final String GETTING_MSG = "Getting {}";

    private TeacherDAO teacherDAO;
    private SubjectDAO subjectDAO;
    private TeacherMapperDTO teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO,
                              TeacherMapperDTO teacherMapper,
                              SubjectDAO subjectDAO) {
        this.teacherDAO = teacherDAO;
        this.teacherMapper = teacherMapper;
        this.subjectDAO = subjectDAO;
    }

    @Override
    public void addTeacher(TeacherDTO teacherDTO) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastName = %s",
                teacherDTO.getFirstName(), teacherDTO.getLastName());
        LOGGER.debug("Adding {}", teacherMessage);
        try {
            teacherDAO.save(teacherMapper.teacherDTOToTeacher(teacherDTO));
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
        TeacherDTO teacher = teacherMapper.getTeacherDTO(teacherDAO.getTeacherById(teacherId));
        if (teacher == null) {
            String failMessage = String.format("Failed to get %s", getMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved {}", getMessage);
        return teacher;
    }

    @Override
    @Transactional
    public TeacherDTO getTeacherByName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug(GETTING_MSG, teacherMessage);
        TeacherDTO teacher;
        try {
            teacher = teacherMapper.getTeacherDTO(teacherDAO.getTeacherByName(firstName, lastName));
        } catch (NoResultException e) {
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            LOGGER.debug(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
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
            allTeachers.addAll(teacherMapper.getTeacherDTOList(teacherDAO.getAllTeachers()));
            LOGGER.info("Successfully query for {}", teachersMSG);
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all teachers from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allTeachers;
    }

    @Override
    public void updateTeacher(TeacherDTO teacherDTO) {
        Teacher newTeacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        Teacher teacherWithSubjects = teacherDAO.getTeacherById(newTeacher.getId());
        newTeacher.setSubjects(teacherWithSubjects.getSubjects());
        String teacherMessage = String.format("Teacher with ID = %d and firstName = %s, lastname = %s", newTeacher.getId(),
                newTeacher.getFirstName(), newTeacher.getLastName());
        LOGGER.debug("Updating '{}'", teacherMessage);
        try {
            teacherDAO.save(newTeacher);
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
    public void deleteTeacherById(long teacherId) {
        String teacherMessage = String.format("Teacher by id %d", teacherId);
        LOGGER.debug("Deleting '{}'", teacherMessage);
        try {
            teacherDAO.deleteTeacherById(teacherId);
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
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        Subject subject = subjectDAO.getSubjectById(subjectId);
        teacher.getSubjects().add(subject);
        return teacherMapper.getTeacherDTO(teacherDAO.save(teacher));
    }

    @Override
    @Transactional
    public TeacherDTO unassignSubjectFromTeacher(long teacherId, int subjectId) {
        LOGGER.debug("Unassigning subject with id %d to teacher with ");
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        Subject subject = subjectDAO.getSubjectById(subjectId);
        teacher.getSubjects().remove(subject);
        return teacherMapper.getTeacherDTO(teacherDAO.save(teacher));
    }

    @Override
    @Transactional
    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName) {
        String teacherMessage = String.format("Teacher with searching parameters firstName = %s, lastname = %s", firstName, lastName);
        LOGGER.debug(GETTING_MSG, teacherMessage);
        List<TeacherDTO> teachers = new ArrayList<>();
        try {
            teachers.addAll(teacherMapper.getTeacherDTOList(teacherDAO.getTeacherByLikeName(firstName, lastName)));
        } catch (PersistenceException e) {
            String failGetByIdMessage = String.format("Couldn't find %s", teacherMessage);
            throw new DataOperationException(failGetByIdMessage, e);
        }
        return teachers;
    }

}
