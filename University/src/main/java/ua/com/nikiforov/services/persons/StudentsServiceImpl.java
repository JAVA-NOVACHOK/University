package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

@Service
public class StudentsServiceImpl implements StudentsService {

    private StudentDAO studentDAO;
    private GroupDAO groupDAO;

    @Autowired
    public StudentsServiceImpl(StudentDAO studentDAO, GroupDAO groupDAO) {
        this.studentDAO = studentDAO;
        this.groupDAO = groupDAO;
    }

    @Override
    public void addStudent(StudentDTO student) {
        try {
            studentDAO.addStudent(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate room while adding");
        }
    }

    @Override
    public StudentDTO getStudentById(long studentId) {
        return getStudentDTO(studentDAO.getStudentById(studentId));
    }

    public StudentDTO getStudentByName(String firstName, String lastName) {

        return getStudentDTO(studentDAO.getStudentByName(firstName, lastName));
    }

    public StudentDTO getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        return getStudentDTO(studentDAO.getStudentByNameGroupId(firstName, lastName, groupId));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        return getListStudentDTO(students);
    }

    @Override
    public void updateStudent(StudentDTO student) {
        try {
            studentDAO.updateStudent(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Couldn't update student, already exists!", e);
        }
    }

    @Override
    public void deleteStudentById(long studentId) {
        studentDAO.deleteStudentById(studentId);
    }

    @Override
    public void transferStudent(long studentId, long groupIdTo) {
        Student student = studentDAO.getStudentById(studentId);
        StudentDTO newStudent = new StudentDTO(studentId, student.getFirstName(), student.getLastName(), groupIdTo);
        try {
            studentDAO.updateStudent(newStudent);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate student while transferring!", e);
        }
    }

    private static StudentDTO getStudentDTO(Student student) {
        long id = student.getId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        long groupId = student.getGroupId();
        return new StudentDTO(id, firstName, lastName, groupId);
    }
//    private static Student getStudent(StudentDTO studentDTO) {
//        long id = studentDTO.getId();
//        String firstName = studentDTO.getFirstName();
//        String lastName = studentDTO.getLastName();
//        Group group = groupDAO.getGroupId();
//        return new Student(id, firstName, lastName, groupId);
//    }

    public static List<StudentDTO> getListStudentDTO(List<Student> students) {
        List<StudentDTO> studensDTO = new ArrayList<>();
        for (Student student : students) {
            studensDTO.add(getStudentDTO(student));
        }
        return studensDTO;
    }

//    public static List<Student> getListStudent(List<StudentDTO> studentsDTO) {
//        List<Student> studens = new ArrayList<>();
//        for (StudentDTO studentDTO : studentsDTO) {
//            studens.add(getStudent(studentDTO));
//        }
//        return studens;
//    }

}
