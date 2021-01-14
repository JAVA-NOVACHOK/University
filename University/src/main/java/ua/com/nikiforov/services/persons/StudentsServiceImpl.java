package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.repositories.persons.StudentRepository;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.StudentMapperDTO;
import ua.com.nikiforov.models.persons.Student;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class StudentsServiceImpl implements StudentsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsServiceImpl.class);


    private static final String GETTING = "Getting {}";
    private static final String SUCCESSFULLY_RETRIEVED_STUDENT = "Successfully retrieved student '{}'";

    private StudentRepository studentRepository;
    private StudentMapperDTO studentMapper;

    @Autowired
    public StudentsServiceImpl(StudentRepository studentRepository, StudentMapperDTO studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public void addStudent(StudentDTO studentDTO) {
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        String studentMessage = String.format("Student with firstName = %s, lastName = %s, groupId = %d", studentDTO.getFirstName(),
                studentDTO.getLastName(), studentDTO.getGroupId());
        LOGGER.debug("Adding {}", studentMessage);
        try {
            studentRepository.save(student);
            LOGGER.debug("{} added successfully", studentMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + studentMessage);
        }
    }

    @Override
    @Transactional
    public StudentDTO getStudentById(long studentId) {
        String getMessage = String.format("Student by id %s", studentId);
        LOGGER.debug(GETTING, getMessage);
        Student student;
        try {
            student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format("Fail to get student by Id %d from DB", studentId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return studentMapper.studentToStudentDTO(student);
    }

    @Override
    public StudentDTO getStudentByName(String firstName, String lastName) {
        String studentMessage = String.format("Student with firstName = %s, lastName = %s", firstName, lastName);
        LOGGER.debug(GETTING, studentMessage);
        Student student;
        try {
            student = studentRepository.getStudentByFirstNameAndLastName(firstName, lastName)
                    .orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format("Fail to get student by firstName = %s and lastName = %s from DB", firstName, lastName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return studentMapper.studentToStudentDTO(student);
    }

    @Override
    public StudentDTO getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        String studentMessage = String.format(
                "Student with firstName = %s, lastname = %s, groupId = %d",
                firstName, lastName, groupId);
        LOGGER.debug(GETTING, studentMessage);
        Student student;
        try {
            student = studentRepository.getStudentByNameGroupId(groupId, firstName, lastName)
                    .orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format(
                    "Fail to get student by groupId %d, firstName = %s and lastName = %s from DB",
                    groupId, firstName, lastName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info(SUCCESSFULLY_RETRIEVED_STUDENT, student);
        return studentMapper.studentToStudentDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        LOGGER.debug("Getting all students");
        List<StudentDTO> allStudents = new ArrayList<>();
        try {
            allStudents.addAll(studentMapper.getStudentDTOList(studentRepository.findAll()));
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
    public void updateStudent(StudentDTO studentDTO) {
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        LOGGER.debug("Updating {}", studentDTO);
        try {
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Couldn't update student, already exists!", e);
        } catch (PersistenceException e) {
            String failMessage = String.format("Failed to update %s", studentDTO);
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
            studentRepository.deleteById(studentId);
            LOGGER.info("Successful deleting {}", studentMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = "Failed to delete " + studentMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }

    }

    @Override
    @Transactional
    public void transferStudent(long studentId, long groupIdTo) {
        Student student = studentRepository.getOne(studentId);
        StudentDTO newStudent = new StudentDTO(studentId, student.getFirstName(), student.getLastName(), groupIdTo);
        try {
            studentRepository.save(studentMapper.studentDTOToStudent(newStudent));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate student while transferring!", e);
        }
    }

}